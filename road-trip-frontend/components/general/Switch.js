import { Component } from "react";

/**
 * This is a switch component that acts like a switch statement
 * within JSX. Requires case prop on each child to match
 * the selection prop when you want that child to display.
 * Only displays the child where selection === case
 * @param {Object} props The props passed to this object
 * @param {any} props.selection  The selected case
 * @param {Component[]} props.children The children, each having a case prop
 * @returns
 */
const Switch = ({ selection, children }) => {
    // filter out only children with a matching prop
    return children.find((child) => {
        return child.props.case === selection;
    });
};

export default Switch;
