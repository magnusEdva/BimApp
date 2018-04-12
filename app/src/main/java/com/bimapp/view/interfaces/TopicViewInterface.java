package com.bimapp.view.interfaces;

public interface TopicViewInterface extends ViewMVP {
   public interface TopicListener{

   }

   void registerListener(TopicListener listener);
   void unregisterListener();
   void setTopic();
}
