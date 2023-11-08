import Map, {
  Layer,
  MapLayerMouseEvent,
  Source,
  ViewStateChangeEvent,
} from "react-map-gl";
import { geoLayer, overlayData } from "./overlays";
import React, { useEffect, useState } from "react";
// You need to make this private file api.ts yourself!
import { ACCESS_TOKEN } from "../private/api";
import "../styles/main.css";

interface LatLong {
  lat: number;
  long: number;
}

function MapBox() {
  const ProvidenceLatLong: LatLong = { lat: 50.824, long: 30.4128 };
  const initialZoom = 0.5;
  const [overlay, setOverlay] = useState<GeoJSON.FeatureCollection | undefined>(
    undefined
  );

  const [viewState, setViewState] = useState({
    longitude: ProvidenceLatLong.long,
    latitude: ProvidenceLatLong.lat,
    zoom: initialZoom,
  });

  function onMapClick(e: MapLayerMouseEvent) {
    console.log(e);
    console.log(e.lngLat.lat);
    console.log(e.lngLat.lng);
  }

  useEffect(() => {
    setOverlay(overlayData());
  }, []);

  return (
    <div className="mapbox">
        <Map
          mapboxAccessToken={ACCESS_TOKEN}
          longitude={viewState.longitude}
          latitude={viewState.latitude}
          zoom={viewState.zoom}
          onMove={(ev: ViewStateChangeEvent) => setViewState(ev.viewState)}
          mapStyle={"mapbox://styles/mapbox/light-v11"}
          style={{ width: 500, height: 400 }}
          onClick={(ev: MapLayerMouseEvent) => onMapClick(ev)}
        >
          <Source id="geo_data" type="geojson" data={overlay}>
            <Layer
              id={geoLayer.id}
              type={geoLayer.type}
              paint={geoLayer.paint}
            />
          </Source>
        </Map>
    </div>
  );
}

export default MapBox;
