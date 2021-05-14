// Tell the code inspection tool that we're writing ES6 compliant code:
// jshint esversion: 6
// Tell the code inspection tool that we're using "developer" classes (console, alert, etc)
// jshint devel:true
// See https://jshint.com/docs/ for more JSHint directives
// jshint unused:false

class UserDataTableRow extends React.Component {
    render() {
        return (
            <tr>
                <td>{this.props.firstName}</td>
                <td>{this.props.lastName}</td>
                <td>{this.props.age}</td>
                <td>{this.props.favoriteColor}</td>
            </tr>
        )
    }
}

class UserDataTable extends React.Component {
    render() {
        const tableRows = this.props.items.map((entry) =>
            (<UserDataTableRow
                firstName = {entry.firstName}
                lastName = {entry.lastName}
                age = {entry.age}
                favoriteColor = {entry.favoriteColor}
            />)
        );
        return (
            <table className="table table-bordered table-striped">
                <thead>
                    <tr className="table-dark">
                        <th className="firstcol">First Name</th>
                        <th>Last Name</th>
                        <th>Age</th>
                        <th>Favorite Color</th>
                    </tr>
                </thead>
                <tbody>
                {tableRows}
                </tbody>
            </table>
        );
    }
}