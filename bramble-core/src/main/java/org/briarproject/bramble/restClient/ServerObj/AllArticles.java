package org.briarproject.bramble.restClient.ServerObj;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Winterhart on 4/15/2018.
 * This class is a singleton that will have all articles...
 */

public class AllArticles {

    private static List<Article> instanceAllArticles;

    protected AllArticles(){};

    public static List<Article> getInstanceAllArticles(){
        if(instanceAllArticles == null){
            instanceAllArticles = new ArrayList<>();
        }
        return instanceAllArticles;
    }
}
