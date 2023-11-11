import Map, {
  Layer,
  MapLayerMouseEvent,
  Source,
  ViewStateChangeEvent,
} from "react-map-gl";
import { geoLayer, overlayData } from "./overlays";
import React, { useEffect, useState,Dispatch,
  SetStateAction, } from "react";
// You need to make this private file api.ts yourself!
import { ACCESS_TOKEN } from "../private/api";
import "../styles/main.css";

/**
 * Props interface to share the state with REPLInput
 */
export interface MapBoxProps{
  file: string;
  setFile: Dispatch<SetStateAction<string>>;
}

interface LatLong {
  lat: number;
  long: number;
}

/**
 * Main function for the MapBox JSX element
 * @param props 
 * @returns 
 */
function MapBox(props: MapBoxProps) {
  const ProvidenceLatLong: LatLong = { lat: 40.69, long: -73.82 };
  const initialZoom = 10;
  const [overlay, setOverlay] = useState<GeoJSON.FeatureCollection | undefined>(
    undefined
  );

  const [viewState, setViewState] = useState({
    longitude: ProvidenceLatLong.long,
    latitude: ProvidenceLatLong.lat,
    zoom: initialZoom,
  });

  /**
   * function from the maps gear up 
   * @param e 
   */
  function onMapClick(e: MapLayerMouseEvent) {
    console.log(e);
    console.log(e.lngLat.lat);
    console.log(e.lngLat.lng);
  }

  /**
   * useEffect to rerender the overlay based on changes with our link
   */

  useEffect(() => {
    async function doStuff() {
      const data = await overlayData(props.file);
      setOverlay(data);
    }
    doStuff();
  }, [props.file]);

  return (
    <div className="mapbox">
      <Map
        mapboxAccessToken={ACCESS_TOKEN}
        longitude={viewState.longitude}
        latitude={viewState.latitude}
        zoom={viewState.zoom}
        onMove={(ev: ViewStateChangeEvent) => setViewState(ev.viewState)}
        mapStyle={"mapbox://styles/mapbox/light-v11"}
        style={{
          position: "relative",
          top: "1px",
          left: "500px",
          width: "600px",
          height: "600px",
          zIndex: 1,
        }}
        onClick={(ev: MapLayerMouseEvent) => onMapClick(ev)}
      >
        <Source id="geo_data" type="geojson" data={overlay}>
          {/* {console.log(overlay)} */}
          <Layer id={geoLayer.id} type={geoLayer.type} paint={geoLayer.paint} />
        </Source>
      </Map>
    </div>
  );
}

export default MapBox;
