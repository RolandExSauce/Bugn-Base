import { useState, useEffect, useRef } from "react";

interface CarouselPropTypes {
  imgUrls: string[];
}

export default function Carousel({ imgUrls }: CarouselPropTypes) {
  const [activeIndex, setActiveIndex] = useState(0);
  const sliderRef = useRef<HTMLDivElement>(null);
  const intervalRef = useRef<number | undefined>(undefined);
  const [reversed, setReversed] = useState(false);

  useEffect(() => {
    startSlide();
    return () => clearInterval(intervalRef.current);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [imgUrls.length]);

  const startSlide = () => {
    clearInterval(intervalRef.current);
    intervalRef.current = window.setInterval(() => {
      setActiveIndex((current) => {
        if (current === imgUrls.length - 1) {
          setReversed(true);
          return current - 1;
        } else if (current === 0) {
          setReversed(false);
          return current + 1;
        } else {
          return current + (reversed ? -1 : 1);
        }
      });
    }, 5000);
  };

  const stopSlide = () => {
    clearInterval(intervalRef.current);
  };

  useEffect(() => {
    if (sliderRef.current) {
      const newTranslate = activeIndex * -100;
      sliderRef.current.style.transform = `translateX(${newTranslate}%)`;
    }
  }, [activeIndex]);

  const prevSlide = () => {
    const prevIndex = (activeIndex - 1 + imgUrls.length) % imgUrls.length;
    setActiveIndex(prevIndex);
  };

  const nextSlide = () => {
    const nextIndex = (activeIndex + 1) % imgUrls.length;
    setActiveIndex(nextIndex);
  };

  return (
    <div
      onMouseEnter={stopSlide}
      onMouseLeave={startSlide}
      className="carousel_slider_container position-relative overflow-hidden w-100 h-100 border rounded d-flex justify-content-center overflow-y-hidden p-3 bg-white"
    >
      <div
        ref={sliderRef}
        style={{ transition: "transform 0.5s ease-out", display: "flex" }}
      >
        {imgUrls.map((url, index) => (
          <img
            key={index}
            src={import.meta.env.VITE_BASE_URL + "/media" + url}
            alt={`Slide ${index + 1}`}
          />
        ))}
      </div>
      <button
        disabled={activeIndex === 0}
        onClick={prevSlide}
        className="carousel_prev_btn position-absolute bottom-0 border-0 z-2"
      >
        <img src="/arrow-bck.svg" alt="" />
      </button>
      <button
        disabled={activeIndex === imgUrls.length - 1}
        onClick={nextSlide}
        className="carousel_next_btn position-absolute bottom-0 border-0 z-2"
      >
        <img src="/arrow-frw.svg" alt="" />
      </button>
    </div>
  );
}
