import Cookies from "js-cookie";

const username = "username";

export function getUser() {
  return Cookies.get(username);
}

export function setUser(username: string) {
  return Cookies.set(username, username);
}

export function removeUser() {
  return Cookies.remove(username);
}
