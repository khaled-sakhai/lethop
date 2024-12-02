import React from 'react'
import styles from './logo.module.css'
import Image from 'next/image'
import logo from "public/assets/logo.png"

export default function Logo() {
  console.log(logo)
  return (
    <>
    <h1 className={styles.logo}><a href="/"><Image src={logo} width={150} alt={'Mo3afir logo'}/></a></h1>
        </>
  )
}
