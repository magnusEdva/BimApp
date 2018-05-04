package com.bimapp.view.interfaces;

import com.bimapp.model.entity.Topic;
import com.bimapp.view.interfaces.ViewMVP;

import java.util.List;

/**
 * Interface defining the contracts between the View and the Presenter.
 * <p>
 * The {@link TopicsViewToPresenter} interface is implemented by the Presenter, and is called
 * when something is selected by the view.
 * <p>
 * The other methods are implemented by the View.
 * The {@link registerListener} method lets the View know which method to callback with {@link onSelectedItem}.
 * The {@link setTopics} method is called by the Presenter when it has received a list of topics.
 */

public interface TopicsViewInterface extends ViewMVP {

    interface TopicsViewToPresenter {
        void onSelectedItem(Topic topic);
        void onSearch(String argument, String searchString);
    }

    void registerListener(TopicsViewToPresenter topicsViewToPresenter);
    void unregisterListener();
    void setTopics(List<Topic> topics);
    void clearSearch();
}
