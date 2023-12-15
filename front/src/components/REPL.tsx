import { useState } from "react";
import "../styles/main.css";
import { REPLHistory } from "./REPLHistory";
import { REPLInput } from "./REPLInput";
import MapBox from "./MapBox";
import GoogleMapFunc from "./GoogleMaps";
import Places from "./GoogleMaps";
import UserBox from "./User";
/**
 * This is the main REPL class. It binds history and the REPLInput together.
 */

export default function REPL() {
  const [history, setHistory] = useState<string[][][]>([[[]]]); // history that records everything as a table
  const [REPLfile, REPLsetFile] = useState<string[][]>([[]]);
  const [file, setFile] = useState<string>(
    "http://localhost:2020/boundbox?filepath=data/census/fullDownload.json&minlat=-40&minlon=-120&step=0"
  );

  return (
    <div className="repl" aria-label="High component">
      {/* <MapBox 
      file={file}
      setFile={setFile}
      /> */}
      {/* <GoogleMapFunc /> */}
      <Places />
      <UserBox />
      <REPLHistory commands={history} />
      <REPLInput
        commands={history}
        file={REPLfile}
        setFile={REPLsetFile}
        setHistory={setHistory}
        setOverlay={setFile}
      />
    </div>
  );
}
