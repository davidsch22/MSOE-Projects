// Class: SE2840 - Lab 8 - React Bus Tracker
// Name: David Schulz
// Class Section: 041

// Tell the code inspection tool that we're writing ES6 compliant code:
// jshint esversion: 6
// Tell the code inspection tool that we're using "developer" classes (console, alert, etc)
// jshint devel:true
// See https://jshint.com/docs/ for more JSHint directives
// jshint unused:false

class BusTrackerTableRow extends React.Component {
    render() {
        return (
            <tr>
                <td>{this.props.bus}</td>
                <td>{this.props.des}</td>
                <td>{this.props.lat}</td>
                <td>{this.props.lon}</td>
                <td>{this.props.spd}</td>
                <td>{this.props.dist}</td>
            </tr>
        )
    }
}

class BusTrackerTable extends React.Component {
    render() {
        const route = this.props.route;
        const tableRows = this.props.items.map((entry) =>
            (<BusTrackerTableRow
                bus = {entry.bus}
                des = {entry.des}
                lat = {entry.lat}
                lon = {entry.lon}
                spd = {entry.spd}
                dist = {entry.dist}
            />)
        );
        return (
            <table className="table">
                <thead>
                <tr>
                    <th>Bus</th>
                    <th>Route {route}</th>
                    <th>latitude</th>
                    <th>longitude</th>
                    <th>speed(MPH)</th>
                    <th>dist(mi)</th>
                </tr>
                </thead>
                <tbody>
                    {tableRows}
                </tbody>
            </table>
        );
    }
}