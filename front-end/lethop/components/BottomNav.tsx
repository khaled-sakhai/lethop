"use client";

import { usePathname } from "next/navigation";
import Link from "next/link";
import { FC } from "react";
type props = {
  setSideBare: any;
  showSideBare: boolean;
  isLoggedIn: boolean;
};
export default function BottomNav({
  setSideBare,
  showSideBare,
  isLoggedIn,
}: props) {
  const pathname = usePathname();

  console.log(pathname);
  const navItems = [
    {
      onclick: () => {
        setSideBare((prev: boolean) => !prev);
      },
      label: "القائمة",
      icon: HamburgerIcon,
      show: true,
    },
    {
      href: "/",
      label: "الرئيسية",
      icon: HomeIcon,
      show: true,
    },
    {
      href: "/posts/new",
      label: "إضافة منشور",
      icon: AddIcon,
      show: isLoggedIn,
    },
    {
      href: "/auth/profile",
      label: "حسابي",
      icon: MyAccountIcon,
      show: isLoggedIn,
    },
    {
      href: "/auth/register",
      label: "تسجيل",
      icon: SignUpIcon,
      show: !isLoggedIn,
    },
    { id:1,href: "/auth/login", label: "دخول", icon: LoginIcon, show: !isLoggedIn },
  ];

  return (
    <nav
      className={`fixed bottom-0 left-0 w-full bg-white border-t border-gray-4 block z-[1001] lg:z-0 lg:hidden`}
    >
      <div className="flex justify-around items-center py-2 flex-wrap">
        {navItems.map(
          ({ href, label, icon: Icon, show, onclick }) =>
            show &&
            (href ? (
              <Link
                key={label}
                href={href}
                className={`flex flex-col items-center font-mob  ${
                  pathname === href ? "text-dark" : "text-gray"
                } hover:text-dark`}
              >
                <Icon />
                <span>{label}</span>
              </Link>
            ) : (
              <button
                key={label}
                onClick={onclick}
                className={`flex flex-col items-center font-mob  ${
                  pathname === href ? "text-dark" : "text-gray"
                } hover:text-dark`}
              >
                <Icon />
                <span>{label}</span>
              </button>
            ))
        )}
      </div>
    </nav>
  );
}

const HomeIcon = () => (
  <svg
    xmlns="http://www.w3.org/2000/svg"
    fill="none"
    viewBox="0 0 24 24"
    strokeWidth={2}
    stroke="currentColor"
    className="w-8 h-8"
  >
    <path
      strokeLinecap="round"
      strokeLinejoin="round"
      d="M3 10.5l9-7 9 7M5.25 21h13.5a2.25 2.25 0 002.25-2.25v-7.5a2.25 2.25 0 00-.957-1.848l-8.25-6.417a.75.75 0 00-.866 0l-8.25 6.417A2.25 2.25 0 003 11.25v7.5A2.25 2.25 0 005.25 21z"
    />
  </svg>
);

const HamburgerIcon = () => (
  <svg
    xmlns="http://www.w3.org/2000/svg"
    fill="none"
    viewBox="0 0 24 24"
    strokeWidth={2}
    stroke="currentColor"
    className="w-8 h-8"
  >
    <path
      strokeLinecap="round"
      strokeLinejoin="round"
      d="M4 6h16M4 12h16M4 18h16"
    />
  </svg>
);

const AddIcon = () => (
  <svg
    xmlns="http://www.w3.org/2000/svg"
    fill="none"
    viewBox="0 0 24 24"
    strokeWidth={2}
    stroke="currentColor"
    className="w-8 h-8"
  >
    <path strokeLinecap="round" strokeLinejoin="round" d="M12 4v16m8-8H4" />
  </svg>
);
const SignUpIcon = () => (
  <svg
    xmlns="http://www.w3.org/2000/svg"
    fill="none"
    viewBox="0 0 24 24"
    strokeWidth={2}
    stroke="currentColor"
    className="w-8 h-8"
  >
    <path
      strokeLinecap="round"
      strokeLinejoin="round"
      d="M12 4v8m0 0l4-4m-4 4l-4-4M5 12h14a2 2 0 012 2v6a2 2 0 01-2 2H5a2 2 0 01-2-2v-6a2 2 0 012-2z"
    />
  </svg>
);

const MyAccountIcon = () => (
  <svg
    xmlns="http://www.w3.org/2000/svg"
    fill="none"
    viewBox="0 0 24 24"
    strokeWidth={2}
    stroke="currentColor"
    className="w-8 h-8"
  >
    <path
      strokeLinecap="round"
      strokeLinejoin="round"
      d="M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z"
    />
  </svg>
);

const SearchIcon = () => (
  <svg
    xmlns="http://www.w3.org/2000/svg"
    fill="none"
    viewBox="0 0 24 24"
    strokeWidth={2}
    stroke="currentColor"
    className="w-8 h-8"
  >
    <path
      strokeLinecap="round"
      strokeLinejoin="round"
      d="M10.5 18.75a8.25 8.25 0 108.25-8.25m-5.64 9.14a5.25 5.25 0 110-10.5 5.25 5.25 0 010 10.5z"
    />
  </svg>
);

const LoginIcon = () => (
  <svg
    xmlns="http://www.w3.org/2000/svg"
    fill="none"
    viewBox="0 0 24 24"
    strokeWidth={2}
    stroke="currentColor"
    className="w-8 h-8"
  >
    <path
      strokeLinecap="round"
      strokeLinejoin="round"
      d="M15.75 9V4.5a2.25 2.25 0 00-2.25-2.25h-6a2.25 2.25 0 00-2.25 2.25V9m12 0a2.25 2.25 0 012.25 2.25v6a2.25 2.25 0 01-2.25 2.25h-6a2.25 2.25 0 01-2.25-2.25v-6A2.25 2.25 0 019.75 9m6 0H9.75"
    />
  </svg>
);
