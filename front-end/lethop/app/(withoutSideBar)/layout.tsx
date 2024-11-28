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

const marhey = Marhey({ subsets: ["arabic"] });

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  const [showSideBar, setShowSideBar] = React.useState<boolean>(true);
  const [isSmallScreen, setIsSmallScreen] = useState(false);

  useEffect(() => {
    const handleResize = () => {
      const isSmall = window.innerWidth < 1280;
      setIsSmallScreen(isSmall);
      if (!isSmall) {
        // Ensure show is false when screen width is greater than 1024px
        setShowSideBar(true);
      }
      if (isSmall) {
        setShowSideBar(false);
      }
    };

    // Initial check
    handleResize();

    // Add event listener
    window.addEventListener("resize", handleResize);

    // Cleanup event listener on unmount
    return () => window.removeEventListener("resize", handleResize);
  }, []);

  return (
    <html lang="en" dir="rtl">
      <body className={`${marhey.className} ${styles.layout}`}>
        <Provider>
          <Header show={showSideBar} setShow={setShowSideBar} />

          <section className={styles.content}>
            <RightBar
              show={showSideBar}
              small={isSmallScreen}
              setter={setShowSideBar}
            />

            <main className={styles.main}>
              {children}
              <p className="py-10">done! </p>
              <p className="py-10">done! </p>
              <p className="py-10">done! </p>
              <p className="py-10">done! </p>
              <p className="py-10">done! </p>
              <p className="py-10">done! </p>
              <p className="py-10">done! </p>
              <p className="py-10">done! </p>
              <p className="py-10">done! </p>
              <p className="py-10">done! </p>
              <p className="py-10">done! </p>
              <p className="py-10">done! </p>
              <p className="py-10">done! </p>
              <p className="py-10">done! </p>
              <p className="py-10">done! </p>
              <p className="py-10">done! </p>
              <p className="py-10">done! </p>
              <p className="py-10">done! </p>
              <p className="py-10">done! </p>
              <p className="py-10">done! </p>
              <p className="py-10">done! </p>
            </main>
          </section>
        </Provider>
      </body>
    </html>
  );
}
