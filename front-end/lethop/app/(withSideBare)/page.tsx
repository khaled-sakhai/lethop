import Link from 'next/link';
import type { Metadata } from 'next';
import Tags from '@/components/main/filterBar/Tags';
import Category from '@/components/main/filterBar/Category';
import FilterBar from '@/components/main/FilterBar';
import Image from 'next/image';
import Posts from '@/components/main/Posts';

export const metadata: Metadata = {
	title: 'Full Auth | Home',
	description: 'Full Auth home page',
};

export default function Page() {
	return (
		<>	
				<section>ADD new post</section>

			<FilterBar />
			<Posts />
			
		</>
	);
}
