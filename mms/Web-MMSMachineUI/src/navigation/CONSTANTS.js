export const ROOT = "/";
export const ROUTE_LOGIN = "/login";
export const ROUTE_OEEDASHBOARD = "/dashboard";

export const GetCurrentMainRoute = (pathname) => {
  if (pathname.startsWith(ROUTE_OEEDASHBOARD)) return ROUTE_OEEDASHBOARD;
  // if (pathname.startsWith(ROUTE_PROCESSMONITOR)) return ROUTE_PROCESSMONITOR;
  return ROOT;
};
