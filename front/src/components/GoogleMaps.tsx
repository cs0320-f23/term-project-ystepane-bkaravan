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
    const changeDirection = (origin, destination) => {
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
            console.log(origin);
            console.log(destination);
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

  function submitForm() {
    //document.getElementById("origin-new").value = selected;
    var form = document.getElementById("myForm");
    var formData = new FormData(form);

    fetch("http://localhost:2020/dateform", {
      method: "POST",
      body: formData,
    })
      .then((response) => {
        // Handle the response as needed
        console.log("Form submitted successfully:", response);
        // You can update the current page or perform other actions here
      })
      .catch((error) => {
        console.error("Error submitting form:", error);
      });
  }

  console.log(directions);
  return (
    <>
      <form id="myForm">
        <div className="time-submit">
          <label htmlFor="pickuptime" className="pickup">
            Pickup (date and time):
          </label>
          <input
            type="datetime-local"
            id="pickuptime"
            name="pickuptime"
            required
          />
          <input type="submit" value="Next" onClick={() => submitForm()} />
          <input type="hidden" name="origin-new" id="origin-new" />
          <input type="hidden" name="origin-lat" id="origin-lat" />
          <input type="hidden" name="origin-lon" id="origin-lon" />
          <input type="hidden" name="dest-new" id="dest-new"/>
          <input type="hidden" name="dest-lat" id="dest-lat"/>
          <input type="hidden" name="dest-lon" id="dest-lon"/>


        </div>
        <div className="places-container">
          <PlacesAutocomplete setSelected={setSelected} isOrigin={true} />
        </div>
      </form>

      <div className="places-container-dest">
        <PlacesAutocomplete setSelected={setSelectedDest} isOrigin={false}/>
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

const PlacesAutocomplete = ({ setSelected, isOrigin }) => {
  const {
    ready,
    value,
    setValue,
    suggestions: { status, data },
    clearSuggestions,
  } = usePlacesAutocomplete();

  const handleSelect = async (address: any) => {
    setValue(address, false);
    if (isOrigin) {
      document.getElementById("origin-new").value = address;
    } else {
      document.getElementById("dest-new").value = address;
    }
    console.log(address);
    clearSuggestions();

    const results = await getGeocode({ address });
    const { lat, lng } = await getLatLng(results[0]);
    if (isOrigin) {
      document.getElementById("origin-lat").value = lat;
      document.getElementById("origin-lon").value = lng;
    } else {
      document.getElementById("dest-lat").value = lat;
      document.getElementById("dest-lon").value = lng;
    }
    setSelected({ lat, lng });
  };

  return (
    <Combobox onSelect={handleSelect}>
      <ComboboxInput
        type="travel"
        value={value}
        onChange={(e) => setValue(e.target.value)}
        disabled={!ready}
        className="combobox-input"
        placeholder="Search an address"
        required
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
