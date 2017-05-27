package com.rahmitufanoglu.retrofitdemo.di;


import com.rahmitufanoglu.retrofitdemo.MainActivity2;

import javax.inject.Singleton;

import dagger.Component;


@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {

    void inject(MainActivity2 target);
}
