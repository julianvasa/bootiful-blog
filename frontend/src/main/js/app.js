var React = require('react');
var ReactDOM = require('react-dom');
var rest = require('rest');
import {SigninJsx} from './form.jsx';

class App extends React.Component {

	constructor(props) {
		super(props);
	}

	render() {
		return (
        <form>
        <FormGroup
          controlId="formBasicText"
          validationState={this.getValidationState()}
        >
          <ControlLabel>Working example with validation</ControlLabel>
          <FormControl
            type="text"
            value={this.state.value}
            placeholder="Enter text"
            onChange={this.handleChange}
          />
          <FormControl.Feedback />
          <HelpBlock>Validation is based on string length.</HelpBlock>
        </FormGroup>
      </form>
      		)
	}
}

ReactDOM.render(
  <App />,
  document.getElementById('app')
);