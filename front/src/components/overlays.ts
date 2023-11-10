import { FeatureCollection } from "geojson";
import { FillLayer } from "react-map-gl";

const basic_data =
  "http://localhost:2020/boundbox?filepath=data/census/fullDownload.json&minlat=-40&minlon=-120&step=0";

export async function fetchData(endpoint: string) {
  const response = await fetch(endpoint)
    .then((response) => response.json())
    .then((json) => (isFeatureCollection(json) ? json : undefined));

  // Use data as needed within the scope
  // Return the resolved data directly
  //setData(response);
  return response;
}

function isFeatureCollection(json: any): json is FeatureCollection {
  return json.type === "FeatureCollection";
}

export async function overlayData(file: string) {
  const rl_data = await fetchData(file);
  console.log(file)
  return isFeatureCollection(rl_data) ? rl_data : undefined;
}

//
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
