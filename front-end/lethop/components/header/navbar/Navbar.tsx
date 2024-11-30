import React from "react";
import styles from "./navbar.module.css";
import Link from "next/link";
import Extra from "./Extra";

export default function Navbar() {
  interface NavbarItem {
    id: number;
    name: string;
    url: string;
  }

  const navbarItems: NavbarItem[] = [
    { id: 1, name: "دخول", url: "/auth/login" },
    { id: 2, name: "تسجيل", url: "/auth/register" },
  ];

  return (
    <nav className={styles.navbar}>
      <ul>
        <button>
          {" "}
          <span>تحميل التطبيق</span>
          <svg
            xmlns="http://www.w3.org/2000/svg"
            fill="none"
            viewBox="0 0 24 24"
            strokeWidth="1.5"
            stroke="currentColor"
            className="w-6 h-6 p-2"
          >
            <path
              strokeLinecap="round"
              strokeLinejoin="round"
              d="M10.5 1.5H8.25A2.25 2.25 0 0 0 6 3.75v16.5a2.25 2.25 0 0 0 2.25 2.25h7.5A2.25 2.25 0 0 0 18 20.25V3.75a2.25 2.25 0 0 0-2.25-2.25H13.5m-3 0V3h3V1.5m-3 0h3m-3 18.75h3"
            />
          </svg>
        </button>

        {navbarItems.map((e, i) => {
          return (
            <li key={e.id} className="hidden md:block">
              <Link className={styles.links} href={e.url}>
                {e.name}
              </Link>
            </li>
          );
        })}

        <Extra />
      </ul>
    </nav>
  );
}
