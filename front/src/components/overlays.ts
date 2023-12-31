import { FeatureCollection } from "geojson";
import { FillLayer } from "react-map-gl";


/**
 * Helper function to get the json from the fetch
 * @param endpoint link for the new overlay
 * @returns a promise of appropriate json
 */
export async function fetchData(endpoint: string) {
  const response = await fetch(endpoint)
    .then((response) => response.json())
    .then((json) => (isFeatureCollection(json) ? json : undefined));

  return response;
}

function isFeatureCollection(json: any): json is FeatureCollection {
  return json.type === "FeatureCollection";
}

/**
 * Helper function to support new overlayData
 * 
 * @param file a link to fetch for the new overlay
 * @returns overlay data if the json is of our needed form
 */

export async function overlayData(file: string) {
  const rl_data = await fetchData(file);
  console.log(file)
  return isFeatureCollection(rl_data) ? rl_data : undefined;
}


const propertyName = "holc_grade";
export const geoLayer: FillLayer = {
  id: "geo_data",
  type: "fill",
  paint: {
    "fill-color": [
      "match",
      ["get", propertyName],
      "A",
      "#5bcc04",
      "B",
      "#04b8cc",
      "C",
      "#e9ed0e",
      "D",
      "#d11d1d",
      "H",
      "#800080",
      "#ccc",
    ],
    "fill-opacity": 0.2,
  },
};
