package com.example.prm1.repository

import com.google.android.gms.maps.model.LatLng

interface LocationRepository {
    var savedLocation: LatLng?
}