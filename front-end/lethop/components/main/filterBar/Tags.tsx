import React from 'react'
import styles from './tags.module.css'
import Link from 'next/link';
import {TagIcon}  from '@heroicons/react/24/outline';

export default function Tags() {

    interface TagItem {
        id: number;
        name: string;
        url: string;
      }

const tags: TagItem[] = [
    { id: 1, name: 'ادمان', url: '/'},
    { id: 2, name: 'صحة', url: '/'},
    { id: 3, name: 'نجاح', url: '/'},
    { id: 4, name: 'رياضة', url: '/'},
    { id: 5, name: 'لغات', url: '/'},
    { id: 6, name: 'نجاح مالي', url: '/'},
    { id: 7, name: 'هجرة', url: '/'},
    { id: 8, name: 'علاقات و عائلة', url: '/'},
    { id: 9, name: 'صحة نفسية', url: '/'},
    { id: 10, name: 'دراسة', url: '/'},
    { id: 11, name: 'أمل و تحفيز', url: '/'},
    { id: 12, name: 'صحة نفسية', url: '/'},
    { id: 13, name: 'دراسة', url: '/'},
    { id: 14, name: 'أمل و تحفيز', url: '/'},
  ];

  
  return (
    <section className='px-2 py-6 overflow-x-auto min-w-0	 bar'>
        
        <ul className='flex gap-x-3 gap-y-6 p-y-10'>

{tags.map((e,i)=>{return(
    <li key={e.id} className='py-1'>
        <Link href={e.url} className='flex items-center	gap-2 border rounded-xl text-orange-4 transition duration-150 hover:text-white bg-white hover:bg-orange-4  px-2 py-1 text-nowrap	'>
          <TagIcon className='size-8'/> {e.name}
        </Link>
    </li>
);})}
</ul>


    </section>
  )
}
