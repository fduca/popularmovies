package com.cudaf.myappportfolio.view.fragment;

import java.util.List;

/**
 * Created by cudaf on 29/01/2016.
 */
public interface OnTaskCompleted<T> {

    void onTaskCompleted(List<T> results);

}
