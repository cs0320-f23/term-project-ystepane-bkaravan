import { FeatureCollection } from "geojson";
import { FillLayer } from "react-map-gl";

// Import the raw JSON file
//import  from "../geodata/fullDownload.json"; // this is what we need to fetch from our backend

const basic_data =
  "http://localhost:2020/boundbox?filepath=data/census/fullDownload.json&minlat=-40&minlon=-120&step=0";

async function fetchData() {
  const response = await fetch(basic_data)
    .then((response) => response.json())
    .then((json) => (isFeatureCollection(json) ? json : undefined));

  // Use data as needed within the scope
  console.log(response);

  // Return the resolved data directly
  return response;
}

function isFeatureCollection(json: any): json is FeatureCollection {
  // console.log(json);
  // console.log(json.type);
  console.log(json.type === "FeatureCollection");
  return json.type === "FeatureCollection";
}

export async function overlayData() {
  const rl_data = await fetchData();
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
      "#f1f1f1",
      "#ccc",
    ],
    "fill-opacity": 0.2,
  },
};
