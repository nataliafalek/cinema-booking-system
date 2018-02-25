const url = `http://localhost:8080/`;

export function fetchJson(path) {
    return (fetch(url + path)
        .then(results => {
            return results.json();
        }));

}

export function postJson(path, body) {
    return (fetch(url + path, {
        method: "POST",
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(body)
    }));
}
