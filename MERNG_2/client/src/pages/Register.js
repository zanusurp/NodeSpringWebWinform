import React,{useContext,useState} from 'react'
import { Form, Button } from 'semantic-ui-react';
import { useMutation } from '@apollo/client';
import gql from 'graphql-tag';

import { AuthContext } from '../context/auth';
import { useForm } from '../util/hooks';

function Register(props) {
    const context = useContext(AuthContext);
    const [errors, setErrors] = useState({}); //emptyh

   
    const { onChange, onSubmit, values } = useForm(registerUser, {
        username:'',
        password:'',
        confirmPassword:'',
        email:''
    });

  
    const [addUser, { loading }] = useMutation(REGISTER_USER,{
        update(_, {data: { register: userData}}){ //1번쨰 파라는 proxy인데 필요 없으므로 _  
            console.log(userData);
            props.history.push('/');
        },
        onError(err){
            console.log(err.graphQLErrors[0].extensions.exceptions.errors);
            setErrors(err.graphQLErrors[0].extensions.exceptions.errors);
        },
        variables:values
    });

    function registerUser(){
        addUser();
    }

    
    return (
        <div className="form-container">
            <Form onSubmit={onSubmit} noValidate className={loading ? 'loading' : ''}>
                <h1>Register</h1>
                <Form.Input 
                    label='Username'
                    placeholder='Username..'
                    name='username'
                    type='text'
                    value={values.username}
                    error = {errors.username ? true : false}
                    onChange={onChange}
                />
                <Form.Input 
                    label='Password'
                    placeholder='Password..'
                    name='password'
                    type='password'
                    value={values.password}
                    error = {errors.password ? true : false}
                    onChange={onChange}
                />
                <Form.Input 
                    label='Confirm Password'
                    placeholder='Confirm Password..'
                    name='confirmPassword'
                    type='password'
                    value={values.confirmPassword}
                    error = {errors.confirmPassword ? true : false}
                    onChange={onChange}
                />
                <Form.Input 
                    label='Email'
                    placeholder='Email..'
                    name='email'
                    type='email'
                    value={values.email}
                    error = {errors.email ? true : false}
                    onChange={onChange}
                />
                <Button type='submit' primary>
                    Register
                </Button>
            </Form>
            {/*에러 표현 */}
            {Object.keys(errors).length > 0 && (
                <div className="ui error message">
                <ul className="list">
                    {Object.values(errors).map(value =>(
                        <li key={value} >{value}</li> 
                    ))}
                </ul>
            </div>
            )}
            
        </div>
    );
}

const REGISTER_USER = gql`
    mutation register(
        $username:String!
        $password:String!
        $confirmPassword:String!
        $email:String!
    ){
        register(
            registerInput:{
                username: $username
                password: $password
                confirmPassword: $confirmPassword
                email: $email

            }
        ){
            id email username createdAt token
        }
    }
`;

export default Register;
