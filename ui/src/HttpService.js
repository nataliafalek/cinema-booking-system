// const url = `http://localhost:8080/`;
const url = `/`;

export function fetchJson(path) {
  return (fetch(url + path, {
    method: 'GET',
    credentials: 'include'
  }).then(results => {
    return results.json();
  }));

}

export function postJson(path, body) {
  return (fetch(url + path, {
    method: "POST",
    credentials: 'include',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(body)
  }));
}

export function post(path) {
  return (fetch(url + path, {
    method: "POST",
    credentials: 'include'

  }));
}