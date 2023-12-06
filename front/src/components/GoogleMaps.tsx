// import React from "react";
// import { GoogleMap, useLoadScript, Marker } from "@react-google-maps/api";

// const libraries = ["places"];
// const mapContainerStyle = {
//   width: "45vw",
//   height: "50vh",
// };
// const center = {
//   lat: 41.825226, // default latitude
//   lng: -71.418884, // default longitude
// };

// const GoogleMapFunc = () => {
//   const { isLoaded, loadError } = useLoadScript({
//     googleMapsApiKey: "AIzaSyC9AgZLJoATBEYX9rkoFowN1896XuRLf9s&libraries=places",
//     libraries: ["places"],
//   });

//   if (loadError) {
//     return <div>Error loading maps</div>;
//   }

//   if (!isLoaded) {
//     return <div>Loading maps</div>;
//   }

//   return (
//     <>
//     <div>
//         <div className="places-container">
//             <PlacesAutocomplete setSelected = {setSelected}/>
//         </div>
//       <GoogleMap
//         mapContainerStyle={mapContainerStyle}
//         zoom={10}
//         center={center}
//       >
//         {selected && <Marker position={selected} />}
//       </GoogleMap>
//     </div>
//     </>
//   );
// };

// const PlacesAutocomplete = ({setSelected}) => {
//     return <></>;
// }

// export default GoogleMapFunc;

import { useState, useMemo, useEffect } from "react";
import {
  GoogleMap,
  useLoadScript,
  Marker,
  DirectionsRenderer,
} from "@react-google-maps/api";
import usePlacesAutocomplete, {
  getGeocode,
  getLatLng,
} from "use-places-autocomplete";
import {
  Combobox,
  ComboboxInput,
  ComboboxPopover,
  ComboboxList,
  ComboboxOption,
} from "@reach/combobox";
import "@reach/combobox/styles.css";
import { ACCESS_TOKEN_2 } from "../private/api";

export default function Places() {
  const { isLoaded } = useLoadScript({
    googleMapsApiKey: ACCESS_TOKEN_2,
    libraries: ["places"],
  });

  if (!isLoaded) return <div>Loading...</div>;
  return <Map />;
}

function Map() {
  const center = useMemo(() => ({ lat: 41.825226, lng: -71.418884 }), []);
  const [selected, setSelected] = useState(null);
  const [selectedDest, setSelectedDest] = useState(null);
  const [directions, setDirections] = useState(null); // Use useState for directions
  const directionsService = useMemo(
    () => new window.google.maps.DirectionsService(),
    []
  );

  useEffect(() => {
    const changeDirection = (origin: string, destination: string) => {
      console.log(origin);
      console.log(destination);
      directionsService.route(
        {
          origin: origin,
          destination: destination,
          travelMode: google.maps.TravelMode.DRIVING,
        },
        (result, status) => {
          if (status === google.maps.DirectionsStatus.OK) {
            setDirections(result);
            console.log(directions);
          } else {
            console.error("error fetching directions" + result);
          }
        }
      );
    };
    if (selected && selectedDest) {
      changeDirection(selected, selectedDest);
    }
  }, [selected, selectedDest, directionsService]);

  console.log("directions below");
  console.log(directions);
  return (
    <>
      <div className="places-container">
        <PlacesAutocomplete setSelected={setSelected} />
      </div>

      <div className="places-container-dest">
        <PlacesAutocomplete setSelected={setSelectedDest} />
      </div>

      <GoogleMap
        zoom={10}
        center={center}
        mapContainerClassName="map-container"
      >
        {selected && <Marker position={selected} />}
        {selectedDest && <Marker position={selectedDest} />}
        {selected && selectedDest && (
          <DirectionsRenderer directions={directions} />
        )}
      </GoogleMap>
    </>
  );
}

const PlacesAutocomplete = ({ setSelected }) => {
  const {
    ready,
    value,
    setValue,
    suggestions: { status, data },
    clearSuggestions,
  } = usePlacesAutocomplete();

  const handleSelect = async (address: any) => {
    setValue(address, false);
    console.log(address);
    clearSuggestions();

    const results = await getGeocode({ address });
    const { lat, lng } = await getLatLng(results[0]);
    setSelected({ lat, lng });
  };

  return (
    <Combobox onSelect={handleSelect}>
      <ComboboxInput
        value={value}
        onChange={(e) => setValue(e.target.value)}
        disabled={!ready}
        className="combobox-input"
        placeholder="Search an address"
      />
      <ComboboxPopover>
        <ComboboxList>
          {status === "OK" &&
            data.map(({ place_id, description }) => (
              <ComboboxOption key={place_id} value={description} />
            ))}
        </ComboboxList>
      </ComboboxPopover>
    </Combobox>
  );
};
