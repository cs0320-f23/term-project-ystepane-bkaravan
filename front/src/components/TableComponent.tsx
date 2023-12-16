import React from "react";
/**
 * This component handles the table for history.
 */

/**
 * This interface includes the prop used in the component.
 */
interface Props {
  data: string[][];
}
/**
 * This component is used to create a table out of the string[][] `data`.
 */
const TableComponent: React.FC<Props> = ({ data }) => {
  const minCellWidth = "25x";
  const maxCellWidth = "250px";
  const minCellHeight = "100px";
  const maxCellHeight = "300px";
  return (
    <table
      className="centered-table"
      style={{
        borderSpacing: "5px",
      }}
    >
      <tbody tabIndex={0}>
        {data.map((row, rowIndex) => (
          <tr key={rowIndex}>
            {row.map((cell, cellIndex) => (
              <td
                key={cellIndex}
                style={{
                  margin: "5px",
                  overflow: "hidden",
                  whiteSpace: "pre-wrap",
                  minWidth: minCellWidth,
                  maxWidth: maxCellWidth,
                  minHeight: minCellHeight,
                  maxHeight: maxCellHeight,
                }}
              >
                {cell}
              </td>
            ))}
          </tr>
        ))}
      </tbody>
    </table>
  );
};

export default TableComponent;
