
import { BrowserRouter, Route, Switch } from "react-router-dom";
import "./App.css";
import LoginPage from "./pages/loginPage/LoginPage";

import RegisterPage from "./pages/RegisterPage/RegisterPage";
import Wrapper from "./pages/Wrapper/Wrapper";
import { USER_SITE } from "services/common/urls";
function App() {
  return (
    <div style={{ width: "100%", height: "100%" }}>
      <BrowserRouter>
        <Switch>
          <Route exact path={`${USER_SITE}/login`} component={LoginPage} />
          <Route
            exact
            path={`${USER_SITE}/register`}
            component={RegisterPage}
          />
          <Route exact path={`${USER_SITE}/`} component={LoginPage} />
          <Route path="*" component={Wrapper} />
        </Switch>
      </BrowserRouter>
    </div>
  );
}

export default App;
