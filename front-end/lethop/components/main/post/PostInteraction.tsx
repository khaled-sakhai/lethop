import React from 'react'
import styles from './postInteraction.module.css'
export default function PostInteraction() {
  return (
    <div className={styles.post_interaction}>


        <div className={styles.post_interaction_right}>
          <button className={styles.post_interaction_button}>تعليق</button>
          <button className={styles.post_interaction_button}>اعجاب</button>
          <button className={styles.post_interaction_button}>حفظ</button>
        </div>

        <div className={styles.post_interaction_left}>
            left
        </div>

      </div>
  )
}
