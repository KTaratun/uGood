package com.example.kurt.ugood.classes.Interfaces;

public interface ProfilePicResultHandler<T> {
   void onSuccess(T data);
   void onFailure(Exception e);
}
