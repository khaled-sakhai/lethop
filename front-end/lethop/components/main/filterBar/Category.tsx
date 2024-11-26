import React from 'react'
import styles from './category.module.css'
import Link from 'next/link';
import {TagIcon}  from '@heroicons/react/24/outline';
export default function Category() {
    interface TagItem {
        id: number;
        name: string;
        url: string;
      }
      
const tags: TagItem[] = [
    { id: 1, name: 'تحفيز', url: '/'},
    { id: 2, name: 'أخطاء و ندم', url: '/'},
    { id: 3, name: 'أسئلة و اجوبة', url: '/'},
  ];

  return (
    <section className='border-b border-1  border-gray'>
   <ul className='flex gap-6  p-y-10 '>

{tags.map((e,i)=>{return(
    <li key={e.id} className='py-3 '>
        <Link href={e.url} className='text-2xl  p-2  font-bold transition duration-150 text-gray hover:text-dark border-b-[5px] border-transparent hover:border-green  text-nowrap	'>
           {e.name}
        </Link>
    </li>
);})}
</ul>

    </section>
  )
}
