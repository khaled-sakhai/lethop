'use client';

import { makeStore } from "./store";
import { Provider } from "react-redux";

interface props {
    children: React.ReactNode;
}
export default function CustomProvider({children}:props){
    return <Provider store={makeStore()}>
        {children}
    </Provider>
}