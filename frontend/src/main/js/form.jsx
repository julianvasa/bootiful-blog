import React from 'react';

 export const SigninJsx = () => {
 return (
 <div className="container">
        <form className="form-signin">
            <h2 className="form-signin-heading"> Please sign in </h2>
            <br />
            <label htmlFor="inputEmail" className="sr-only"> Email address
            </label>
            <input type="email" id="inputEmail" onChange={this.handleEmailChange} className="form-control" placeholder="Email address" required autoFocus />
            <br />
            <label htmlFor="inputPassword" className="sr-only"> Password</label>
            <input type="password" id="inputPassword" onChange={this.handlePasswordChange} className="form-control" placeholder="Password" required />
            <br />
            <button className="btn btn-lg btn-primary btn-block" onClick={this.signIn} type="button"> Sign in
            </button>
        </form>
    </div>
)
}