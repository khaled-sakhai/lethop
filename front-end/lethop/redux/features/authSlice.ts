import { createSlice, PayloadAction } from "@reduxjs/toolkit";

interface AuthState{
    isAuthenticated:boolean;
    isLoading:boolean;
    accessToken:string;
}


const initialState = {
    isAuthenticated:false,
    isLoading:true,
    accessToken:'',
} as AuthState;


const authSlice=createSlice({
    name:'auth',
    initialState,
    reducers:{
        setAuth:(state,action:PayloadAction<string>)=>{
            state.accessToken=action.payload;
            state.isAuthenticated=true;
        },
        logout:state=>{
            state.isAuthenticated=false;
        },
        finishInitialLoad:state=>{
            state.isLoading=false;
        }
    }
})

export const {setAuth,logout,finishInitialLoad}= authSlice.actions;
export default authSlice.reducer;