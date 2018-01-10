package org.briarproject.briar.android.keyagreement;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import org.briarproject.bramble.api.nullsafety.MethodsNotNullByDefault;
import org.briarproject.bramble.api.nullsafety.ParametersNotNullByDefault;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import static android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT;
import static android.hardware.Camera.Parameters.FLASH_MODE_OFF;
import static android.hardware.Camera.Parameters.FOCUS_MODE_AUTO;
import static android.hardware.Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE;
import static android.hardware.Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO;
import static android.hardware.Camera.Parameters.FOCUS_MODE_EDOF;
import static android.hardware.Camera.Parameters.FOCUS_MODE_FIXED;
import static android.hardware.Camera.Parameters.FOCUS_MODE_MACRO;
import static android.hardware.Camera.Parameters.SCENE_MODE_AUTO;
import static android.hardware.Camera.Parameters.SCENE_MODE_BARCODE;
import static java.util.logging.Level.INFO;
import static java.util.logging.Level.WARNING;

@SuppressWarnings("deprecation")
@MethodsNotNullByDefault
@ParametersNotNullByDefault
public class CameraView extends SurfaceView implements SurfaceHolder.Callback,
		AutoFocusCallback {

	private static final int AUTO_FOCUS_RETRY_DELAY = 5000; // Milliseconds
	private static final Logger LOG =
			Logger.getLogger(CameraView.class.getName());

	@Nullable
	private Camera camera = null;
	private PreviewConsumer previewConsumer = null;
	private Surface surface = null;
	private int displayOrientation = 0, surfaceWidth = 0, surfaceHeight = 0;
	private boolean previewStarted = false, autoFocus = false;

	public CameraView(Context context) {
		super(context);
	}

	public CameraView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CameraView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@UiThread
	public void setPreviewConsumer(PreviewConsumer previewConsumer) {
		LOG.info("Setting preview consumer");
		this.previewConsumer = previewConsumer;
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		setKeepScreenOn(true);
		getHolder().addCallback(this);
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		setKeepScreenOn(false);
		getHolder().removeCallback(this);
	}

	@UiThread
	public void start() throws CameraException {
		LOG.info("Opening camera");
		try {
			camera = Camera.open();
		} catch (RuntimeException e) {
			throw new CameraException(e);
		}
		if (camera == null) throw new CameraException("No back-facing camera");
		setDisplayOrientation(0);
		// Use barcode scene mode if it's available
		Parameters params = camera.getParameters();
		params = setSceneMode(camera, params);
		if (SCENE_MODE_BARCODE.equals(params.getSceneMode())) {
			// If the scene mode enabled the flash, try to disable it
			if (!FLASH_MODE_OFF.equals(params.getFlashMode()))
				params = disableFlash(camera, params);
			// If the flash is still enabled, disable the scene mode
			if (!FLASH_MODE_OFF.equals(params.getFlashMode()))
				params = disableSceneMode(camera, params);
		}
		// Use the best available focus mode, preview size and other options
		params = setBestParameters(camera, params);
		// Enable auto focus if the selected focus mode uses it
		enableAutoFocus(params.getFocusMode());
		// Log the parameters that are being used (maybe not what we asked for)
		logCameraParameters();
		// Start the preview when the camera and the surface are both ready
		if (surface != null && !previewStarted) startPreview(getHolder());
	}

	@UiThread
	public void stop() throws CameraException {
		if (camera == null) return;
		stopPreview();
		LOG.info("Releasing camera");
		try {
			camera.release();
		} catch (RuntimeException e) {
			throw new CameraException(e);
		}
		camera = null;
	}

	@UiThread
	private void startPreview(SurfaceHolder holder) throws CameraException {
		LOG.info("Starting preview");
		if (camera == null) throw new CameraException("Camera is null");
		try {
			camera.setPreviewDisplay(holder);
			camera.startPreview();
			previewStarted = true;
			startConsumer();
		} catch (IOException | RuntimeException e) {
			throw new CameraException(e);
		}
	}

	@UiThread
	private void stopPreview() throws CameraException {
		LOG.info("Stopping preview");
		if (camera == null) throw new CameraException("Camera is null");
		try {
			stopConsumer();
			camera.stopPreview();
		} catch (RuntimeException e) {
			throw new CameraException(e);
		}
		previewStarted = false;
	}

	@UiThread
	private void startConsumer() throws CameraException {
		if (camera == null) throw new CameraException("Camera is null");
		if (autoFocus) {
			try {
				camera.autoFocus(this);
			} catch (RuntimeException e) {
				throw new CameraException(e);
			}
		}
		previewConsumer.start(camera);
	}

	@UiThread
	private void stopConsumer() throws CameraException {
		if (camera == null) throw new CameraException("Camera is null");
		if (autoFocus) {
			try {
				camera.cancelAutoFocus();
			} catch (RuntimeException e) {
				throw new CameraException(e);
			}
		}
		previewConsumer.stop();
	}

	@UiThread
	private void setDisplayOrientation(int rotationDegrees)
			throws CameraException {
		int orientation;
		CameraInfo info = new CameraInfo();
		try {
			Camera.getCameraInfo(0, info);
		} catch (RuntimeException e) {
			throw new CameraException(e);
		}
		if (info.facing == CAMERA_FACING_FRONT) {
			orientation = (info.orientation + rotationDegrees) % 360;
			orientation = (360 - orientation) % 360;
		} else {
			orientation = (info.orientation - rotationDegrees + 360) % 360;
		}
		if (LOG.isLoggable(INFO))
			LOG.info("Display orientation " + orientation + " degrees");
		if (camera == null) throw new CameraException("Camera is null");
		try {
			camera.setDisplayOrientation(orientation);
		} catch (RuntimeException e) {
			throw new CameraException(e);
		}
		displayOrientation = orientation;
	}

	@UiThread
	private Parameters setSceneMode(Camera camera, Parameters params)
			throws CameraException {
		List<String> sceneModes = params.getSupportedSceneModes();
		if (sceneModes == null) return params;
		if (LOG.isLoggable(INFO)) LOG.info("Scene modes: " + sceneModes);
		if (sceneModes.contains(SCENE_MODE_BARCODE)) {
			params.setSceneMode(SCENE_MODE_BARCODE);
			try {
				camera.setParameters(params);
				return camera.getParameters();
			} catch (RuntimeException e) {
				throw new CameraException(e);
			}
		}
		return params;
	}

	@UiThread
	private Parameters disableFlash(Camera camera, Parameters params)
			throws CameraException {
		params.setFlashMode(FLASH_MODE_OFF);
		try {
			camera.setParameters(params);
			return camera.getParameters();
		} catch (RuntimeException e) {
			throw new CameraException(e);
		}
	}

	@UiThread
	private Parameters disableSceneMode(Camera camera, Parameters params)
			throws CameraException {
		params.setSceneMode(SCENE_MODE_AUTO);
		try {
			camera.setParameters(params);
			return camera.getParameters();
		} catch (RuntimeException e) {
			throw new CameraException(e);
		}
	}

	@UiThread
	private Parameters setBestParameters(Camera camera, Parameters params)
			throws CameraException {
		setVideoStabilisation(params);
		setFocusMode(params);
		params.setFlashMode(FLASH_MODE_OFF);
		setPreviewSize(params);
		try {
			camera.setParameters(params);
			return camera.getParameters();
		} catch (RuntimeException e) {
			throw new CameraException(e);
		}
	}

	@UiThread
	private void setVideoStabilisation(Parameters params) {
		if (Build.VERSION.SDK_INT >= 15 &&
				params.isVideoStabilizationSupported()) {
			params.setVideoStabilization(true);
		}
	}

	@UiThread
	private void setFocusMode(Parameters params) {
		List<String> focusModes = params.getSupportedFocusModes();
		if (LOG.isLoggable(INFO)) LOG.info("Focus modes: " + focusModes);
		if (focusModes.contains(FOCUS_MODE_CONTINUOUS_PICTURE)) {
			params.setFocusMode(FOCUS_MODE_CONTINUOUS_PICTURE);
		} else if (focusModes.contains(FOCUS_MODE_CONTINUOUS_VIDEO)) {
			params.setFocusMode(FOCUS_MODE_CONTINUOUS_VIDEO);
		} else if (focusModes.contains(FOCUS_MODE_EDOF)) {
			params.setFocusMode(FOCUS_MODE_EDOF);
		} else if (focusModes.contains(FOCUS_MODE_MACRO)) {
			params.setFocusMode(FOCUS_MODE_MACRO);
		} else if (focusModes.contains(FOCUS_MODE_AUTO)) {
			params.setFocusMode(FOCUS_MODE_AUTO);
		} else if (focusModes.contains(FOCUS_MODE_FIXED)) {
			params.setFocusMode(FOCUS_MODE_FIXED);
		}
	}

	@UiThread
	private void setPreviewSize(Parameters params) {
		if (surfaceWidth == 0 || surfaceHeight == 0) return;
		float idealRatio = (float) surfaceWidth / surfaceHeight;
		boolean rotatePreview = displayOrientation % 180 == 90;
		List<Size> sizes = params.getSupportedPreviewSizes();
		Size bestSize = null;
		float bestScore = 0;
		for (Size size : sizes) {
			int width = rotatePreview ? size.height : size.width;
			int height = rotatePreview ? size.width : size.height;
			float ratio = (float) width / height;
			float stretch = Math.max(ratio / idealRatio, idealRatio / ratio);
			int pixels = width * height;
			float score = pixels / stretch;
			if (LOG.isLoggable(INFO)) {
				LOG.info("Size " + size.width + "x" + size.height
						+ ", stretch " + stretch + ", pixels " + pixels
						+ ", score " + score);
			}
			if (bestSize == null || score > bestScore) {
				bestSize = size;
				bestScore = score;
			}
		}
		if (bestSize != null) {
			if (LOG.isLoggable(INFO))
				LOG.info("Best size " + bestSize.width + "x" + bestSize.height);
			params.setPreviewSize(bestSize.width, bestSize.height);
		}
	}

	@UiThread
	private void enableAutoFocus(String focusMode) {
		autoFocus = FOCUS_MODE_AUTO.equals(focusMode) ||
				FOCUS_MODE_MACRO.equals(focusMode);
	}

	@UiThread
	private void logCameraParameters() throws CameraException {
		if (camera == null) throw new AssertionError();
		if (LOG.isLoggable(INFO)) {
			Parameters params;
			try {
				params = camera.getParameters();
			} catch (RuntimeException e) {
				throw new CameraException(e);
			}
			if (Build.VERSION.SDK_INT >= 15) {
				LOG.info("Video stabilisation enabled: "
						+ params.getVideoStabilization());
			}
			LOG.info("Scene mode: " + params.getSceneMode());
			LOG.info("Focus mode: " + params.getFocusMode());
			LOG.info("Flash mode: " + params.getFlashMode());
			Size size = params.getPreviewSize();
			LOG.info("Preview size: " + size.width + "x" + size.height);
		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		post(() -> {
			try {
				surfaceCreatedUi(holder);
			} catch (CameraException e) {
				if (LOG.isLoggable(WARNING)) LOG.log(WARNING, e.toString(), e);
			}
		});
	}

	@UiThread
	private void surfaceCreatedUi(SurfaceHolder holder) throws CameraException {
		LOG.info("Surface created");
		if (surface != null && surface != holder.getSurface()) {
			LOG.info("Releasing old surface");
			surface.release();
		}
		surface = holder.getSurface();
		// Start the preview when the camera and the surface are both ready
		if (camera != null && !previewStarted) startPreview(holder);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		post(() -> {
			try {
				surfaceChangedUi(holder, w, h);
			} catch (CameraException e) {
				if (LOG.isLoggable(WARNING)) LOG.log(WARNING, e.toString(), e);
			}
		});
	}

	@UiThread
	private void surfaceChangedUi(SurfaceHolder holder, int w, int h)
			throws CameraException {
		if (LOG.isLoggable(INFO)) LOG.info("Surface changed: " + w + "x" + h);
		if (surface != null && surface != holder.getSurface()) {
			LOG.info("Releasing old surface");
			surface.release();
		}
		surface = holder.getSurface();
		surfaceWidth = w;
		surfaceHeight = h;
		if (camera == null) return; // We are stopped
		stopPreview();
		try {
			Parameters params = camera.getParameters();
			setPreviewSize(params);
			camera.setParameters(params);
			logCameraParameters();
		} catch (RuntimeException e) {
			throw new CameraException(e);
		}
		startPreview(holder);
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		post(() -> surfaceDestroyedUi(holder));
	}

	@UiThread
	private void surfaceDestroyedUi(SurfaceHolder holder) {
		LOG.info("Surface destroyed");
		if (surface != null && surface != holder.getSurface()) {
			LOG.info("Releasing old surface");
			surface.release();
		}
		surface = null;
		holder.getSurface().release();
	}

	@Override
	public void onAutoFocus(boolean success, Camera camera) {
		LOG.info("Auto focus succeeded: " + success);
		postDelayed(this::retryAutoFocus, AUTO_FOCUS_RETRY_DELAY);
	}

	@UiThread
	private void retryAutoFocus() {
		try {
			if (camera != null) camera.autoFocus(this);
		} catch (RuntimeException e) {
			LOG.log(WARNING, "Error retrying auto focus", e);
		}
	}
}