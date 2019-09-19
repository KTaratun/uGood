package com.example.kurt.ugood.classes;

public interface ProfilePicResultHandler<T> {
   void onSuccess(T data);
   void onFailure(Exception e);
}
