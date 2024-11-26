import React from 'react'

import styles from './search.module.css'
export default function Search() {
  return (
    <input
    type="search"
    className={styles.search}
    placeholder={'بحث...'}
  />
  )
}
