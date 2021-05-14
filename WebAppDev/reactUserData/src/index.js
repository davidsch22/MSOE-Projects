// Tell the code inspection tool that we're writing ES6 compliant code:
// jshint esversion: 6
// Tell the code inspection tool that we're using "developer" classes (console, alert, etc)
// jshint devel:true
// See https://jshint.com/docs/ for more JSHint directives
// jshint unused:false

const handleSuccess = (response) => {
    console.log(response);
    ReactDOM.render(<UserDataTable items={response.values} />, document.getElementById("userdatatable"));
};

const handleError = () => {
    alert("Umm, something went wrong");
};

$(document).ready(() => {
    $.ajax({
        url: "http://localhost:3000/all",
        success: handleSuccess,
        error: handleError,
    });
});