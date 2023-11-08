import Map, {
  Layer,
  MapLayerMouseEvent,
  Source,
  ViewStateChangeEvent,
} from "react-map-gl";
import { geoLayer, overlayData } from "./overlays";
import React, { useEffect, useState } from "react";
// You need to make this private file api.ts yourself!
import { ACCESS_TOKEN } from "./private/api";

interface LatLong {
  lat: number;
  long: number;
}

function MapBox() {
  const ProvidenceLatLong: LatLong = { lat: 50.824, long: 30.4128 };
  const initialZoom = 8;
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
    <Map
      mapboxAccessToken={ACCESS_TOKEN}
      longitude={viewState.longitude}
      latitude={viewState.latitude}
      zoom={viewState.zoom} 
      onMove={(ev: ViewStateChangeEvent) => setViewState(ev.viewState)}
      mapStyle={"mapbox://styles/mapbox/light-v11"}
      style={{ width: window.innerWidth, height: window.innerHeight }}
      onClick={(ev: MapLayerMouseEvent) => onMapClick(ev)}
    >
      <Source id="geo_data" type="geojson" data={overlay}>
        <Layer id={geoLayer.id} type={geoLayer.type} paint={geoLayer.paint} />
      </Source>
    </Map>
  );
}

export default MapBox;
