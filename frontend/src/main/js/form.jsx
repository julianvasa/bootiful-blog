import React from "react";
import Grid from "@material-ui/core/Grid";
import TextField from "@material-ui/core/TextField";
import { withStyles } from "@material-ui/core/styles";
import Select from "@material-ui/core/Select";
import FormControl from "@material-ui/core/FormControl";
import InputLabel from "@material-ui/core/InputLabel";
import MenuItem from "@material-ui/core/MenuItem";
import Button from "@material-ui/core/Button";

const styles = theme => ({
  root: {
    flexGrow: 1
  },
  paper: {
    padding: theme.spacing.unit * 2,
    textAlign: "center",
    color: theme.palette.text.secondary
  },
  formControl: {
    margin: theme.spacing.unit,
    minWidth: 120
  }
});

class Form extends React.Component {
  state = {
    cat: "",
    cats: [],
    name: "",
    intro: "",
    instruction: "",
    image: "",
    video: "",
    time: "",
    servings: "",
    difficulty: "",
    ingredient1: "",
    ingredient2: "",
    ingredient3: "",
    ingredient4: "",
    ingredient5: "",
    ingredient6: "",
    ingredient7: "",
    ingredient8: "",
    ingredient9: "",
    ingredient10: "",
    ingredient11: "",
    ingredient12: "",
    ingredient13: "",
    ingredient14: "",
    ingredient15: "",
    ingredient16: "",
    ingredient17: "",
    ingredient18: "",
    ingredient19: "",
    ingredient20: ""
  };

  submit = () => {
    fetch("http://localhost/api/newrecipe", {
      credentials: "same-origin",
      method: "POST",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json"
      },
      body: JSON.stringify({
        category_id: this.state.cat,
        intro: this.state.intro,
        difficulty: this.state.difficulty,
        instruction: this.state.instruction,
        name: this.state.name,
        servings: this.state.servings,
        time: this.state.time,
        username: "",
        video: this.state.video,
        image: this.state.image,
        ingredient1: this.state.ingredient1,
        ingredient2: this.state.ingredient2,
        ingredient3: this.state.ingredient3,
        ingredient4: this.state.ingredient4,
        ingredient5: this.state.ingredient5,
        ingredient6: this.state.ingredient6,
        ingredient7: this.state.ingredient7,
        ingredient8: this.state.ingredient8,
        ingredient9: this.state.ingredient9,
        ingredient10: this.state.ingredient10,
        ingredient11: this.state.ingredient11,
        ingredient12: this.state.ingredient12,
        ingredient13: this.state.ingredient13,
        ingredient14: this.state.ingredient14,
        ingredient15: this.state.ingredient15,
        ingredient16: this.state.ingredient16,
        ingredient17: this.state.ingredient17,
        ingredient18: this.state.ingredient18,
        ingredient19: this.state.ingredient19,
        ingredient20: this.state.ingredient20
      })
    }).then(response => {
      if (response.ok) {
        response.text().then(json => {
          window.location.assign(json);
        });
      }
    });
  };

  handleChange = event => {
    switch (event.target.name) {
      case "cat":
        this.setState({
          cat: event.target.value
        });
        break;
      case "name":
        this.setState({
          name: event.target.value
        });
        break;
      case "intro":
        this.setState({
          intro: event.target.value.replace("\\n", "\n")
        });
        break;
      case "instruction":
        this.setState({
          instruction: event.target.value
        });
        break;
      case "image":
        this.setState({
          image: event.target.value
        });
        break;
      case "video":
        this.setState({
          video: event.target.value
        });
        break;
      case "time":
        this.setState({
          time: event.target.value
        });
        break;
      case "servings":
        this.setState({
          servings: event.target.value
        });
        break;
      case "difficulty":
        this.setState({
          difficulty: event.target.value
        });
        break;
      case "ingredient1":
        this.setState({
          ingredient1: event.target.value
        });
        break;
      case "ingredient2":
        this.setState({
          ingredient2: event.target.value
        });
        break;
      case "ingredient3":
        this.setState({
          ingredient3: event.target.value
        });
        break;
      case "ingredient4":
        this.setState({
          ingredient4: event.target.value
        });
        break;
      case "ingredient5":
        this.setState({
          ingredient5: event.target.value
        });
        break;
      case "ingredient6":
        this.setState({
          ingredient6: event.target.value
        });
        break;
      case "ingredient7":
        this.setState({
          ingredient7: event.target.value
        });
        break;
      case "ingredient8":
        this.setState({
          ingredient8: event.target.value
        });
        break;
      case "ingredient9":
        this.setState({
          ingredient9: event.target.value
        });
        break;
      case "ingredient10":
        this.setState({
          ingredient10: event.target.value
        });
        break;
      case "ingredient11":
        this.setState({
          ingredient11: event.target.value
        });
        break;
      case "ingredient12":
        this.setState({
          ingredient12: event.target.value
        });
        break;
      case "ingredient13":
        this.setState({
          ingredient13: event.target.value
        });
        break;
      case "ingredient14":
        this.setState({
          ingredient14: event.target.value
        });
        break;
      case "ingredient15":
        this.setState({
          ingredient15: event.target.value
        });
        break;
      case "ingredient16":
        this.setState({
          ingredient16: event.target.value
        });
        break;
      case "ingredient17":
        this.setState({
          ingredient17: event.target.value
        });
        break;
      case "ingredient18":
        this.setState({
          ingredient18: event.target.value
        });
        break;
      case "ingredient19":
        this.setState({
          ingredient19: event.target.value
        });
        break;
      case "ingredient20":
        this.setState({
          ingredient20: event.target.value
        });
        break;
      default:
        return;
    }
  };

  componentDidMount() {
    fetch("https://app.unegatuaj.com/android.php?type=category")
      .then(res => res.json())
      .then(
        result => {
          this.setState({
            cats: result
          });
        },
        error => {
          console.log(error);
        }
      );
  }

  render() {
    const { classes } = this.props;

    return (
      <Grid container spacing={24}>
        <Grid item xs={12}>
          <TextField
            id="name"
            name="name"
            label="Name"
            onChange={this.handleChange}
            fullWidth
          />
        </Grid>
        <Grid item xs={12}>
          
          <FormControl fullWidth={true}>
            <InputLabel htmlFor="age-simple"> Category </InputLabel>
            <Select
              name="category"
              value={this.state.cat}
              onChange={this.handleChange}
              inputProps={{
                name: "cat"
              }}
            >
              <MenuItem value="">
                <em> None </em>
              </MenuItem>
              {this.state.cats.map(item => (
                <MenuItem value={item.id} key={item.id} {...item}>
                  
                  {item.name}
                </MenuItem>
              ))}
            </Select>
          </FormControl>
        </Grid>
        <Grid item xs={12}>
          <TextField
            id="intro"
            name="intro"
            label="Intro"
            rows={4}
            multiline={true}
            onChange={this.handleChange}
            fullWidth
          />
        </Grid>
        <Grid item xs={12}>
          <TextField
            id="instruction"
            name="instruction"
            label="Instruction"
            rows={4}
            onChange={this.handleChange}
            multiline={true}
            fullWidth
          />
        </Grid>
        <Grid item xs={12}>
          <TextField
            id="image"
            name="image"
            label="Image"
            onChange={this.handleChange}
            fullWidth
          />
        </Grid>
        <Grid item xs={12}>
          <TextField
            id="video"
            name="video"
            label="Video"
            onChange={this.handleChange}
            fullWidth
          />
        </Grid>
        <Grid item xs={12} sm={4}>
          <TextField
            id="time"
            name="time"
            label="Time"
            onChange={this.handleChange}
            fullWidth
          />
        </Grid>
        <Grid item xs={12} sm={4}>
          <TextField
            id="servings"
            name="servings"
            label="Servings"
            onChange={this.handleChange}
            fullWidth
          />
        </Grid>
        <Grid item xs={12} sm={4}>
          <TextField
            id="difficulty"
            name="difficulty"
            label="Difficulty"
            onChange={this.handleChange}
            fullWidth
          />
        </Grid>
        <Grid item xs={12} sm={6}>
          <TextField
            id="ingredient1"
            name="ingredient1"
            label="Ingredient"
            onChange={this.handleChange}
            fullWidth
          />
        </Grid>
        <Grid item xs={12} sm={6}>
          <TextField
            id="ingredient2"
            name="ingredient2"
            label="Ingredient"
            onChange={this.handleChange}
            fullWidth
          />
        </Grid>
        <Grid item xs={12} sm={6}>
          <TextField
            id="ingredient3"
            name="ingredient3"
            label="Ingredient"
            onChange={this.handleChange}
            fullWidth
          />
        </Grid>
        <Grid item xs={12} sm={6}>
          <TextField
            id="ingredient4"
            name="ingredient4"
            label="Ingredient"
            onChange={this.handleChange}
            fullWidth
          />
        </Grid>
        <Grid item xs={12} sm={6}>
          <TextField
            id="ingredient5"
            name="ingredient5"
            label="Ingredient"
            onChange={this.handleChange}
            fullWidth
          />
        </Grid>
        <Grid item xs={12} sm={6}>
          <TextField
            id="ingredient6"
            name="ingredient6"
            label="Ingredient"
            onChange={this.handleChange}
            fullWidth
          />
        </Grid>
        <Grid item xs={12} sm={6}>
          <TextField
            id="ingredient7"
            name="ingredient7"
            label="Ingredient"
            onChange={this.handleChange}
            fullWidth
          />
        </Grid>
        <Grid item xs={12} sm={6}>
          <TextField
            id="ingredient8"
            name="ingredient8"
            label="Ingredient"
            onChange={this.handleChange}
            fullWidth
          />
        </Grid>
        <Grid item xs={12} sm={6}>
          <TextField
            id="ingredient9"
            name="ingredient9"
            label="Ingredient"
            onChange={this.handleChange}
            fullWidth
          />
        </Grid>
        <Grid item xs={12} sm={6}>
          <TextField
            id="ingredient10"
            name="ingredient10"
            label="Ingredient"
            onChange={this.handleChange}
            fullWidth
          />
        </Grid>
        <Grid item xs={12} sm={6}>
          <TextField
            id="ingredient11"
            name="ingredient11"
            label="Ingredient"
            onChange={this.handleChange}
            fullWidth
          />
        </Grid>
        <Grid item xs={12} sm={6}>
          <TextField
            id="ingredient12"
            name="ingredient12"
            label="Ingredient"
            onChange={this.handleChange}
            fullWidth
          />
        </Grid>
        <Grid item xs={12} sm={6}>
          <TextField
            id="ingredient13"
            name="ingredient13"
            label="Ingredient"
            onChange={this.handleChange}
            fullWidth
          />
        </Grid>
        <Grid item xs={12} sm={6}>
          <TextField
            id="ingredient14"
            name="ingredient14"
            label="Ingredient"
            onChange={this.handleChange}
            fullWidth
          />
        </Grid>
        <Grid item xs={12} sm={6}>
          <TextField
            id="ingredient15"
            name="ingredient15"
            label="Ingredient"
            onChange={this.handleChange}
            fullWidth
          />
        </Grid>
        <Grid item xs={12} sm={6}>
          <TextField
            id="ingredient16"
            name="ingredient16"
            label="Ingredient"
            onChange={this.handleChange}
            fullWidth
          />
        </Grid>
        <Grid item xs={12} sm={6}>
          <TextField
            id="ingredient17"
            name="ingredient17"
            label="Ingredient"
            onChange={this.handleChange}
            fullWidth
          />
        </Grid>
        <Grid item xs={12} sm={6}>
          <TextField
            id="ingredient18"
            name="ingredient18"
            label="Ingredient"
            onChange={this.handleChange}
            fullWidth
          />
        </Grid>
        <Grid item xs={12} sm={6}>
          <TextField
            id="ingredient19"
            name="ingredient19"
            label="Ingredient"
            onChange={this.handleChange}
            fullWidth
          />
        </Grid>
        <Grid item xs={12} sm={6}>
          <TextField
            id="ingredient20"
            name="ingredient20"
            label="Ingredient"
            onChange={this.handleChange}
            fullWidth
          />
        </Grid>
        <Grid item xs={12} sm={6}>
          <Button
            variant="contained"
            color="primary"
            onClick={this.submit}
            className={classes.button}
          >
            Submit
          </Button>
        </Grid>
      </Grid>
    );
  }
}

export default withStyles(styles)(Form);
