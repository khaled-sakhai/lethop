"use client";

import "@/styles/globals.css";
import styles from '../layout.module.css';

import { Marhey } from "next/font/google";
import type { Metadata } from "next";
import LeftBar from "@/components/leftBar/LeftBar";
import Header from "@/components/header/Header";
import RightBar from "@/components/rightBar/RightBar";
import { useEffect, useState } from "react";
import React from "react";

import Provider from "../../redux/provider";


export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  const [showSideBar, setShowSideBar] = React.useState<boolean>(false);

 
  return (
    <html lang="en" dir="rtl">
      <body className={` ${styles.layout}`}>
        <Provider>
          <Header show={showSideBar} setShow={setShowSideBar} />

          
            <RightBar
              show={showSideBar}
              setter={setShowSideBar}
            />

           <section className={styles.content}>
<main className={styles.main}>
              {children}
              <p className="py-10">done! </p>
              <p className="py-10">done! </p>
              <p className="py-10">done! </p>
              <p className="py-10">done! </p>
              <p className="py-10">done! </p>
              <p className="py-10">done! </p>
    
             
            </main> 
            <LeftBar/>
</section>
            
         
        </Provider>
      </body>
    </html>
  );
}
