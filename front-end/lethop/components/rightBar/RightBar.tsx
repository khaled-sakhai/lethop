"use client"

import React from 'react'
import { usePathname } from 'next/navigation'
import { useState, ReactNode } from 'react';
import styles from './rightBar.module.css'
import MainMenu from './MainMenu';
import TagsMenu from './TagsMenu';
import WebMenu from './WebMenu';
import ClickAwayListener from 'react-click-away-listener';

  interface barProps{
    show:boolean,
    small:boolean,
    setter:any;
  }

export default function RightBar({show,small,setter}:barProps) {
  
const pathname = usePathname();


  // 
  if((pathname === '/' || pathname === '/post')){

  }
 

  function handleClickAway(event: Event): void {
    if(small){
      setter(false);
    }
  }

  return (
    		<ClickAwayListener onClickAway={handleClickAway}>
    
      <aside className={(show)? `${styles.rightBar} ${`bar`}` : 'hidden'}>
        <MainMenu />
        <TagsMenu />
        <WebMenu />
      </aside>
      </ClickAwayListener>

  

  )
}

