import React from 'react'
import{TagIcon} from '@heroicons/react/24/outline';

import styles from './tagsMenu.module.css'
import Link from 'next/link';
export default function TagsMenu() {
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
      ];


  return (
    
<>
        <h3> <span>مواضيع:</span></h3>
            <ul className='flex flex-wrap gap-x-3 gap-y-6 p-y-10'>

        {tags.map((e,i)=>{return(
            <li key={e.id} className=' py-1'>
                <Link href={e.url} className='border rounded-xl text-orange hover:text-orange-3 bg-orange-3 hover:bg-orange-1 px-2 py-1'>
                    {e.name}
                </Link>
            </li>
        );})}
    </ul>

    </>
  )
}
