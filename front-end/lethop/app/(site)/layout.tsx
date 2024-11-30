"use client";

import "@/styles/globals.css";
import styles from "../layout.module.css";
import { Marhey } from "next/font/google";
import type { Metadata } from "next";
import Header from "@/components/header/Header";
import RightBar from "@/components/rightBar/RightBar";
import { useEffect, useState } from "react";
import React from "react";

import Provider from "../../redux/provider";
import Divider from "@/common/divider";

// const marhey = Marhey({ subsets: ["arabic"] });

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  const [showSideBar, setShowSideBar] = React.useState<boolean>(false);
  const [isSmallScreen, setIsSmallScreen] = useState(true);

  // useEffect(() => {
  //   const handleResize = () => {
  //     const isSmall = window.innerWidth < 1280;
  //     setIsSmallScreen(isSmall);
  //     if (!isSmall) {
  //       // Ensure show is false when screen width is greater than 1024px
  //       setShowSideBar(true);
  //     }
  //     if (isSmall) {
  //       setShowSideBar(false);
  //     }
  //   };

  //   // Initial check
  //   handleResize();

  //   // Add event listener
  //   window.addEventListener("resize", handleResize);

  //   // Cleanup event listener on unmount
  //   return () => window.removeEventListener("resize", handleResize);
  // }, []);

  return (
    <html lang="en" dir="rtl">
      <body className={` ${styles.layout}`}>
        <Provider>
          <Header show={showSideBar} setShow={setShowSideBar} />

          <section className={styles.content}>
            <RightBar
              show={showSideBar}
              setter={setShowSideBar}
            />

            <main className={styles.main}>
              {children}
              
              </main>
           
          </section>
        </Provider>
      </body>
    </html>
  );
}
