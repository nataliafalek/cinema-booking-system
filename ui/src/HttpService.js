const url = process.env.REACT_APP_API_URL;

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