import { fetchData } from "./overlays";

/**
 * This interface sets up the format of the REPLFunction's output.
 */
export interface REPLFunction {
  (args: string[]): Promise<[string, string[][]]>;
}
/**
 * This interface checks the mode.
 */
interface MODEfunction {
  (mode: boolean): string;
}
/**
 * This interface checks the properties of the Load.
 */
interface LoadProperties {
  filepath: string;
}
/**
 * This function checks if the LoadResponse is a valid response that can be processed.
 *
 * @param rjson -- does the filepath exist.
 * @returns -- boolean whether the response is valid.
 */
function isLoadResponse(rjson: any): rjson is LoadProperties {
  if (!("filepath" in rjson)) return false;
  return true;
}
/**
 * This function handles the load by fetching the load query from the backend server.
 *
 * @param args -- arguments that are being handed in, the filepath.
 * @returns -- a Promise, that contains the response, either success or error.
 */
const loadHandler: REPLFunction = (args: string[]) => {
  const url = "http://localhost:2020/loadcsv?filepath=" + args[0];
  return fetch(url).then((response: Response) => {
    return response.json().then((json) => {
      if (isLoadResponse(json)) {
        const output: [string, string[][]] = [json.filepath, []];
        return output;
      }
      return ["Could not find file " + args[0], []];
    });
  });
};
/**
 * This interface checks the properties of view.
 */
interface ViewProperties {
  result: string;
  view_data: string[][];
}

/**
 * This interface checks for bad properties.
 */

interface ViewBadProperties {
  error_type: string;
  type: string;
}

/**
 * This function checks if the response for view is valid.
 *
 * @param rjson -- the response.
 * @returns -- boolean whether the response is valid.
 */
function isViewResponse(
  rjson: ViewBadProperties | ViewProperties
): rjson is ViewProperties {
  //return (rjson as ViewProperties).result !== undefined;
  if (!("result" in rjson)) return false;
  if (!("view_data" in rjson)) return false;

  return true;
}

/**
 * This function handles the view by fetching it from our back-end.
 * @param args -- the argumnents that are getting passed in.
 * @returns -- A promise with a response of success or error.
 */

const viewHandler: REPLFunction = (args: string[]) => {
  const url = "http://localhost:2020/viewcsv";
  return fetch(url).then((response: Response) => {
    return response.json().then((json) => {
      if (isViewResponse(json)) {
        const output: [string, string[][]] = [
          json.result + " view",
          json.view_data,
        ];
        return output;
      } else {
        const output: [string, string[][]] = [json.error_type, []];
        return output;
      }
    });
  });
};

/**
 * This interfaces checks whether the response to search is valid.
 */

interface SearchGoodResponse {
  result: string;
  data: string[][];
}
/**
 * This function checks the validity of the search response.
 *
 * @param rjson -- the response.
 * @returns --boolean whether the response is valid.
 */
function isSearchResponse(rjson: any): rjson is SearchGoodResponse {
  if (!("result" in rjson)) return false;
  if (!("data" in rjson)) return false;
  return true;
}

/**
 * This function handles setting up the arguments and checking whether the lenght is permittable, as well as
 * differentiating between columns and indeces.
 *
 * @param args -- the arguments passed in.
 * @returns -- the "parsed"/processed arguments.
 */

function argSetup(args: string[]): string[] {
  if (args.length < 3) {
    args = [...args, "n"];
  }
  let narrow = args[0];
  let index: number = parseInt(narrow);
  if (Number.isNaN(index)) {
    args[0] = "N:" + narrow;
  } else {
    args[0] = "I:" + narrow;
  }

  return args;
}

/**
 * This function handles search.
 *
 * @param args -- parsed arguments.
 * @returns -- a Promise with a response.
 */
const searchHandler: REPLFunction = (args: string[]) => {
  if (args.length < 2 || args.length > 3) {
    return Promise.resolve([
      "Search expects a column identifier, a target word and an optional header",
      [],
    ]);
  }
  args = argSetup(args);
  console.log(args);
  const url =
    "http://localhost:2020/searchcsv?search=" +
    args[1] +
    "&" +
    args[0] +
    "&" +
    args[2];
  console.log(url);
  return fetch(url).then((response: Response) => {
    return response.json().then((json) => {
      if (isSearchResponse(json)) {
        const output: [string, string[][]] = [
          json.result + " search",
          json.data,
        ];
        return output;
      } else {
        const output: [string, string[][]] = [json.error_type, []];
        return output;
      }
    });
  });
};

/**
 * This interfaces checks for broadband responses.
 */
interface BroadBandGoodResponse {
  result: string;
  address: string;
  bbNumber: string;
  timestamp: string;
}

/**
 * This function checks whether the broadband respones is valid.
 *
 * @param rjson -- the response.
 * @returns -- boolean whether the response is valid.
 */
function isBroadbandResponse(rjson: any): rjson is BroadBandGoodResponse {
  if (!("result" in rjson)) return false;
  if (!("address" in rjson)) return false;
  if (!("bbNumber" in rjson)) return false;
  if (!("timestamp" in rjson)) return false;
  return true;
}

/**
 * This function handles the broadband by calling our back end or displaying error messages.
 * @param args -- the arguments passed in.
 * @returns  -- a Promise with the response.
 */
const broadbandHandler: REPLFunction = (args: string[]) => {
  if (args.length < 2) {
    return Promise.resolve([
      "Not enough args for broadband, expected a state and a county",
      [],
    ]);
  }
  console.log(args);
  const url =
    "http://localhost:2020/broadband?state=" + args[0] + "&county=" + args[1];
  console.log(url);
  return fetch(url).then((response: Response) => {
    return response.json().then((json) => {
      if (isBroadbandResponse(json)) {
        const output: [string, string[][]] = [
          json.result + " broadband",
          [[json.address, "bb number: " + json.bbNumber, json.timestamp]],
        ];
        return output;
      } else {
        const output: [string, string[][]] = [json.error_type, []]; // specify county or state
        return output;
      }
    });
  });
};

/**
 * helper function that supports the highlight command
 * @param args filepath and the search term
 * @returns a promise with the response to load a new overlay
 */

const areaSearchHandler: REPLFunction = (args: string[]) => {
  if (args.length < 2) {
    return Promise.resolve([
      "Not enough args for area search, expected a filepath and a keyword",
      [],
    ]);
  }
  const url =
    "http://localhost:2020/areasearch?filepath=" +
    args[0] +
    "&keyword=" +
    args[1];
  return fetch(url).then((response: Response) => {
    return response
      .json()
      .then((json) => {
        const output: [string, string[][]] = ["change", [[url]]];
        return output;
      })
      .catch(() => {
        return Promise.resolve([
          "Not enough args for area search, expected a filepath and a county",
          [],
        ]);
      });
  });
};

/**
 * This interface checks the properties of the load.
 */

interface LoadProperties {
  reload: string;
}

/**
 * This function checks whether the response to the reload is valid.
 * @param rjson -- the response to check.
 * @returns -- boolean whether the response is valid.
 */
function isReloadResponse(rjson: any): rjson is LoadProperties {
  if (!("reload" in rjson)) return false;
  return true;
}

/**
 * This function handles reloading the page through our back end for the purposes of clean integration testing.
 *
 * @param args -- the arguments
 * @returns -- the response.
 */
const reloadHandler: REPLFunction = (args: string[]) => {
  const url = "http://localhost:2020/reload";
  return fetch(url).then((response: Response) => {
    return response.json().then((json) => {
      if (isReloadResponse(json)) {
        const output: [string, string[][]] = [json.reload, []];
        return output;
      }
      return ["Bad response ", []];
    });
  });
};

interface ShowProperties {
  rides: {}[];
}

function isShowResponse(rjson: any): rjson is ShowProperties {
  if (!("rides" in rjson)) return false;
  return true;
}

interface City {
  name: string;
  lat: number;
  lon: number;
}

function isCity(rjson: any): rjson is City {
  if (!("name" in rjson)) return false;
  if (!("lon" in rjson)) return false;
  if (!("lat" in rjson)) return false;
  return true;
}

interface Guest {
  name: string;
  number: string;
  email: string;
}

function isGuest(rjson: any): rjson is Guest {
  if (!("name" in rjson)) return false;
  if (!("number" in rjson)) return false;
  if (!("email" in rjson)) return false;
  return true;
}

interface Ride {
  rideID: number;
  rideScore: number;
  departureTime: string;
  destination: City;
  guests: Guest[];
  host: Guest;
  origin: City;
  spotsLeft: number;
  type: string;
}

function isRide(rjson: any): rjson is Ride {
  if (!("rideID" in rjson)) return false;
  if (!("rideScore" in rjson)) return false;
  if (!("departureTime" in rjson)) return false;
  if (!("destination" in rjson)) return false;
  if (!("guests" in rjson)) return false;
  if (!("host" in rjson)) return false;
  if (!("origin" in rjson)) return false;
  if (!("spotsLeft" in rjson)) return false;
  if (!("type" in rjson)) return false;
  return true;
}

function addCity(json: City) {
  let toRet: string = "";
  toRet += json.name + " ";
  // toRet += json.lat + " ";
  // toRet += json.lon + " ";
  return toRet;
}

function addGuest(json: Guest) {
  let toRet: string = "";
  toRet += json.name + " ";
  toRet += json.number + " ";
  toRet += json.email + " ";
  return toRet;
}

function printDb(json: ShowProperties) {
  let rides: {}[] = json.rides;
  const beginningSeq: string[] = [
    "RideID",
    "RideScore",
    "Time",
    "Origin",
    "Destination",
    "Host Information",
    "Type",
    "SpotsLeft",
    "Guests",
  ];
  let toRet: string[][] = [];
  if ("pending" in json) {
    toRet[0] = ["Your", "pending", "ride"];
    let pend_ride = json.pending;
    if (isRide(pend_ride)) {
      toRet[1] = [
        pend_ride.departureTime.toString(),
        addCity(pend_ride.origin),
        addCity(pend_ride.destination),
        addGuest(pend_ride.host),
        pend_ride.type,
      ];
      toRet[2] = ["Our Database"];
      toRet[3] = beginningSeq;
    }
  } else {
    toRet[0] = beginningSeq;
  }
  console.log(rides.length);
  for (let i = 0; i < rides.length; i++) {
    let ride = rides[i];
    // console.log(ride);
    let current: string[] = [];
    if (isRide(ride)) {
      current.push(ride.rideID.toString());
      current.push(ride.rideScore.toString());
      current.push(ride.departureTime);
      if (isCity(ride.origin)) {
        current.push(addCity(ride.origin));
      }
      if (isCity(ride.destination)) {
        current.push(addCity(ride.destination));
      }
      if (isGuest(ride.host)) {
        current.push(addGuest(ride.host));
      }
      current.push(ride.type);
      current.push(ride.spotsLeft.toString());
      for (let j = 0; j < ride.guests.length; j++) {
        if (isGuest(ride.guests[j])) {
          current.push(addGuest(ride.guests[j]));
        }
      }
      // console.log("got here");
      // console.log(current);
      toRet.push(current);
    }
  }
  // console.log(toRet);
  return toRet;
}
const showHandler: REPLFunction = (args: string[]) => {
  const url = "http://localhost:2020/startdb";
  return fetch(url).then((response: Response) => {
    return response.json().then((json) => {
      if (isShowResponse(json)) {
        console.log(json.rides);
        const output: [string, string[][]] = ["success!", printDb(json)];
        return output;
      }
      return ["Bad response ", []];
    });
  });
};

interface CreateProperties {
  database: ShowProperties;
}

function isCreateResponse(rjson: any): rjson is CreateProperties {
  if (!("database" in rjson)) return false;
  return true;
}

const createHandler: REPLFunction = (args: string[]) => {
  if (args.length < 2) {
    return Promise.resolve(["Expected use: create <spots> <type>", []]);
  }
  const url =
    "http://localhost:2020/createRide?spots=" + args[0] + "&type=" + args[1];
  console.log(url);
  return fetch(url).then((response: Response) => {
    return response.json().then((json) => {
      if (isCreateResponse(json)) {
        //console.log(json.rides);
        const output: [string, string[][]] = [
          "success!",
          printDb(json.database),
        ];
        return output;
      }
      return [json.error, []]; //double check this
    });
  });
};

const joinHandler: REPLFunction = (args: string[]) => {
  if (args.length < 1) {
    return Promise.resolve(["Expected use: join <rideID>", []]);
  }
  const url = "http://localhost:2020/joinRide?id=" + args[0];
  console.log(url);
  return fetch(url).then((response: Response) => {
    return response.json().then((json) => {
      if (isCreateResponse(json)) {
        //console.log(json.rides);
        const output: [string, string[][]] = [
          "success!",
          printDb(json.database),
        ];
        return output;
      }
      return [json.error, []]; //double check this
    });
  });
};

/**
 * This map contains references from a string representation of a command to an actual function.
 */
const REPLMap: { [key: string]: REPLFunction } = {};
REPLMap["load_file"] = loadHandler;
REPLMap["search"] = searchHandler;
REPLMap["view"] = viewHandler;
REPLMap["broadband"] = broadbandHandler;
REPLMap["reload"] = reloadHandler;
REPLMap["highlight"] = areaSearchHandler;
REPLMap["show"] = showHandler;
REPLMap["create"] = createHandler;
REPLMap["join"] = joinHandler;

/**
 * This function handles the commands that are being passed in.
 *
 * @param command -- the command.
 * @param args -- the arguments.
 * @returns -- an output that is a command with a call of the arguments passed in or an error message for an invalid command.
 */
export function commandHandler(
  command: string,
  args: string[]
): Promise<[string, string[][]]> {
  if (REPLMap[command!]) {
    const output = REPLMap[command](args);
    return output;
  } else {
    return Promise.resolve(["Command " + command + " not found", [[]]]);
  }
}
