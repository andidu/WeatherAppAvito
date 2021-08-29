package com.adorastudios.weatherappavito.data

interface DataSourceProvider {
    fun provideDataSource(): DataSource
}