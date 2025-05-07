import Cookies from "js-cookie";

const userKey = "username";

export function getUser() {
  return Cookies.get(userKey);
}

export function setUser(userName: string) {
  return Cookies.set(userKey, userName);
}

export function removeUser() {
  return Cookies.remove(userKey);
}
