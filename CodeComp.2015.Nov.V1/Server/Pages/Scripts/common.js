function getBootstrapClassTheme(state) {
    switch (state) {
        case "Initiated":
            return "info";
        case "Open":
            return "warning";
        case "Running":
            return "success";
        case "Finished":
            return "primary";
        case "Cancelled":
            return "danger";
    }
}

function MakeBaseAuth(u, p) {

    var tok = u + ':' + p;
    if (!window.btoa)
        window.btoa = $.base64.btoa;
    var hash = btoa(tok);
    return "Basic " + hash;
}