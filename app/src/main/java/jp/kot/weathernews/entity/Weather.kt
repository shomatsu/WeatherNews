package jp.kot.weathernews.entity

import com.github.gfx.android.orma.annotation.Column
import com.github.gfx.android.orma.annotation.Setter
import com.github.gfx.android.orma.annotation.Table

@Table
class Weather(
        @Setter @Column var tempMax: Int? = null,
        @Setter @Column var tempMin: Int? = null,
        @Setter @Column var humidity: Int? = null,
        @Setter @Column var weather: String? = null,
        @Setter @Column var icon: String? = null,
        @Setter @Column var date: String? = null,
        @Setter @Column var dateTime: String? = null)
