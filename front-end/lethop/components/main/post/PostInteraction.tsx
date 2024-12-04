import React from 'react'
import styles from './postInteraction.module.css'
import {ArrowUpCircleIcon,ArrowDownCircleIcon,ChatBubbleOvalLeftIcon,BookmarkIcon,ShareIcon,TagIcon} from '@heroicons/react/24/outline';
import Link from 'next/link';

type props={
  category:string
}


export default function PostInteraction({category}:props) {

  return (
    <div className={styles.post_interaction}>


        <div className={styles.post_interaction_right}>
       
            <button className={styles.post_interaction_category}><TagIcon className='size-8 ' />  {category}</button>


          <button className={styles.post_interaction_button_like}>
            <Link href={"#"}><ArrowDownCircleIcon className='size-10'/></Link>
            <span>31</span>
            <Link href={"#"}><ArrowUpCircleIcon className='size-10'/></Link>
          </button>
          <button className={styles.post_interaction_button}><ChatBubbleOvalLeftIcon className='size-8' />  10</button>
          <button className={styles.post_interaction_button}><BookmarkIcon className='size-8' /> حفظ</button>

        </div>

        <div className={styles.post_interaction_left}>
          <button className={styles.post_interaction_button}><ShareIcon className='size-8' /> مشاركة</button>
        </div>

      </div>
  )
}
