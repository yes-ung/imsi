<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Document</title>
          <link rel="stylesheet" href="css/admin.css" />
      <link rel="stylesheet" href="css/globals.css" />
  </head>
  <body>
    <main>
      <div class="min-h-screen bg-background">
        <section
          class="bg-gradient-to-r from-pink-500 to-orange-500 text-white py-16"
        >
          <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
            <div class="text-center mb-8">
              <div class="flex items-center justify-center mb-4">
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  width="24"
                  height="24"
                  viewBox="0 0 24 24"
                  fill="none"
                  stroke="currentColor"
                  stroke-width="2"
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  class="lucide lucide-search h-8 w-8 mr-3"
                  aria-hidden="true"
                >
                  <path d="m21 21-4.34-4.34"></path>
                  <circle cx="11" cy="11" r="8"></circle>
                </svg>
                <h1 class="text-4xl font-bold">CheForest 통합 검색</h1>
              </div>
              <p class="text-xl opacity-90">
                레시피, 게시글, Q&amp;A를 한 번에 검색하세요
              </p>
            </div>
            <div class="max-w-2xl mx-auto">
              <div class="relative">
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  width="24"
                  height="24"
                  viewBox="0 0 24 24"
                  fill="none"
                  stroke="currentColor"
                  stroke-width="2"
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  class="lucide lucide-search absolute left-4 top-1/2 transform -translate-y-1/2 text-gray-400 h-5 w-5"
                  aria-hidden="true"
                >
                  <path d="m21 21-4.34-4.34"></path>
                  <circle cx="11" cy="11" r="8"></circle></svg
                ><input
                  data-slot="input"
                  class="file:text-foreground placeholder:text-muted-foreground selection:bg-primary selection:text-primary-foreground dark:bg-input/30 border-input flex h-9 min-w-0 px-3 transition-[color,box-shadow] outline-none file:inline-flex file:h-7 file:border-0 file:bg-transparent file:text-sm file:font-medium disabled:pointer-events-none disabled:cursor-not-allowed disabled:opacity-50 md:text-sm focus-visible:border-ring focus-visible:ring-ring/50 focus-visible:ring-[3px] aria-invalid:ring-destructive/20 dark:aria-invalid:ring-destructive/40 aria-invalid:border-destructive w-full pl-12 pr-4 py-4 text-lg bg-white border-0 rounded-xl shadow-lg focus:ring-2 focus:ring-white/50 text-gray-800"
                  placeholder="레시피, 재료, 요리법을 검색해보세요..."
                  value="김치찌개"
                /><button
                  class="absolute right-4 top-1/2 transform -translate-y-1/2 text-gray-400 hover:text-gray-600"
                >
                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    width="24"
                    height="24"
                    viewBox="0 0 24 24"
                    fill="none"
                    stroke="currentColor"
                    stroke-width="2"
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    class="lucide lucide-x h-5 w-5"
                    aria-hidden="true"
                  >
                    <path d="M18 6 6 18"></path>
                    <path d="m6 6 12 12"></path>
                  </svg>
                </button>
              </div>
            </div>
          </div>
        </section>
        <section class="py-8">
          <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
            <div
              class="flex flex-col md:flex-row gap-4 items-center justify-between mb-8"
            >
              <div class="flex items-center space-x-4">
                <button
                  type="button"
                  role="combobox"
                  aria-controls="radix-:r3:"
                  aria-expanded="false"
                  aria-autocomplete="none"
                  dir="ltr"
                  data-state="closed"
                  data-slot="select-trigger"
                  data-size="default"
                  class="border-input data-[placeholder]:text-muted-foreground [&amp;_svg:not([class*='text-'])]:text-muted-foreground focus-visible:border-ring focus-visible:ring-ring/50 aria-invalid:ring-destructive/20 dark:aria-invalid:ring-destructive/40 aria-invalid:border-destructive dark:bg-input/30 dark:hover:bg-input/50 flex items-center justify-between gap-2 rounded-md border bg-input-background px-3 py-2 text-sm whitespace-nowrap transition-[color,box-shadow] outline-none focus-visible:ring-[3px] disabled:cursor-not-allowed disabled:opacity-50 data-[size=default]:h-9 data-[size=sm]:h-8 *:data-[slot=select-value]:line-clamp-1 *:data-[slot=select-value]:flex *:data-[slot=select-value]:items-center *:data-[slot=select-value]:gap-2 [&amp;_svg]:pointer-events-none [&amp;_svg]:shrink-0 [&amp;_svg:not([class*='size-'])]:size-4 w-40"
                >
                  <span data-slot="select-value" style="pointer-events: none"
                    ><span class="flex items-center"
                      ><span class="mr-2">🍽️</span>전체</span
                    ></span
                  ><svg
                    xmlns="http://www.w3.org/2000/svg"
                    width="24"
                    height="24"
                    viewBox="0 0 24 24"
                    fill="none"
                    stroke="currentColor"
                    stroke-width="2"
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    class="lucide lucide-chevron-down size-4 opacity-50"
                    aria-hidden="true"
                  >
                    <path d="m6 9 6 6 6-6"></path>
                  </svg></button
                ><button
                  type="button"
                  role="combobox"
                  aria-controls="radix-:r4:"
                  aria-expanded="false"
                  aria-autocomplete="none"
                  dir="ltr"
                  data-state="closed"
                  data-slot="select-trigger"
                  data-size="default"
                  class="border-input data-[placeholder]:text-muted-foreground [&amp;_svg:not([class*='text-'])]:text-muted-foreground focus-visible:border-ring focus-visible:ring-ring/50 aria-invalid:ring-destructive/20 dark:aria-invalid:ring-destructive/40 aria-invalid:border-destructive dark:bg-input/30 dark:hover:bg-input/50 flex items-center justify-between gap-2 rounded-md border bg-input-background px-3 py-2 text-sm whitespace-nowrap transition-[color,box-shadow] outline-none focus-visible:ring-[3px] disabled:cursor-not-allowed disabled:opacity-50 data-[size=default]:h-9 data-[size=sm]:h-8 *:data-[slot=select-value]:line-clamp-1 *:data-[slot=select-value]:flex *:data-[slot=select-value]:items-center *:data-[slot=select-value]:gap-2 [&amp;_svg]:pointer-events-none [&amp;_svg]:shrink-0 [&amp;_svg:not([class*='size-'])]:size-4 w-32"
                >
                  <span data-slot="select-value" style="pointer-events: none"
                    >관련도순</span
                  ><svg
                    xmlns="http://www.w3.org/2000/svg"
                    width="24"
                    height="24"
                    viewBox="0 0 24 24"
                    fill="none"
                    stroke="currentColor"
                    stroke-width="2"
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    class="lucide lucide-chevron-down size-4 opacity-50"
                    aria-hidden="true"
                  >
                    <path d="m6 9 6 6 6-6"></path>
                  </svg>
                </button>
              </div>
              <div class="text-sm text-muted-foreground">
                '<span class="font-semibold text-orange-600">김치찌개</span>'
                검색 결과 2개
              </div>
            </div>
            <div
              dir="ltr"
              data-orientation="horizontal"
              data-slot="tabs"
              class="flex flex-col gap-2 w-full"
            >
              <div
                role="tablist"
                aria-orientation="horizontal"
                data-slot="tabs-list"
                class="bg-muted text-muted-foreground h-9 items-center justify-center rounded-xl p-[3px] grid w-full max-w-md grid-cols-4"
                tabindex="0"
                data-orientation="horizontal"
                style="outline: none"
              >
                <button
                  type="button"
                  role="tab"
                  aria-selected="true"
                  aria-controls="radix-:r5:-content-all"
                  data-state="active"
                  id="radix-:r5:-trigger-all"
                  data-slot="tabs-trigger"
                  class="data-[state=active]:bg-card dark:data-[state=active]:text-foreground focus-visible:border-ring focus-visible:ring-ring/50 focus-visible:outline-ring dark:data-[state=active]:border-input dark:data-[state=active]:bg-input/30 text-foreground dark:text-muted-foreground inline-flex h-[calc(100%-1px)] flex-1 items-center justify-center gap-1.5 rounded-xl border border-transparent px-2 py-1 text-sm font-medium whitespace-nowrap transition-[color,box-shadow] focus-visible:ring-[3px] focus-visible:outline-1 disabled:pointer-events-none disabled:opacity-50 [&amp;_svg]:pointer-events-none [&amp;_svg]:shrink-0 [&amp;_svg:not([class*='size-'])]:size-4"
                  tabindex="-1"
                  data-orientation="horizontal"
                  data-radix-collection-item=""
                >
                  전체 (2)</button
                ><button
                  type="button"
                  role="tab"
                  aria-selected="false"
                  aria-controls="radix-:r5:-content-recipe"
                  data-state="inactive"
                  id="radix-:r5:-trigger-recipe"
                  data-slot="tabs-trigger"
                  class="data-[state=active]:bg-card dark:data-[state=active]:text-foreground focus-visible:border-ring focus-visible:ring-ring/50 focus-visible:outline-ring dark:data-[state=active]:border-input dark:data-[state=active]:bg-input/30 text-foreground dark:text-muted-foreground inline-flex h-[calc(100%-1px)] flex-1 items-center justify-center gap-1.5 rounded-xl border border-transparent px-2 py-1 text-sm font-medium whitespace-nowrap transition-[color,box-shadow] focus-visible:ring-[3px] focus-visible:outline-1 disabled:pointer-events-none disabled:opacity-50 [&amp;_svg]:pointer-events-none [&amp;_svg]:shrink-0 [&amp;_svg:not([class*='size-'])]:size-4"
                  tabindex="-1"
                  data-orientation="horizontal"
                  data-radix-collection-item=""
                >
                  레시피 (1)</button
                ><button
                  type="button"
                  role="tab"
                  aria-selected="false"
                  aria-controls="radix-:r5:-content-board"
                  data-state="inactive"
                  id="radix-:r5:-trigger-board"
                  data-slot="tabs-trigger"
                  class="data-[state=active]:bg-card dark:data-[state=active]:text-foreground focus-visible:border-ring focus-visible:ring-ring/50 focus-visible:outline-ring dark:data-[state=active]:border-input dark:data-[state=active]:bg-input/30 text-foreground dark:text-muted-foreground inline-flex h-[calc(100%-1px)] flex-1 items-center justify-center gap-1.5 rounded-xl border border-transparent px-2 py-1 text-sm font-medium whitespace-nowrap transition-[color,box-shadow] focus-visible:ring-[3px] focus-visible:outline-1 disabled:pointer-events-none disabled:opacity-50 [&amp;_svg]:pointer-events-none [&amp;_svg]:shrink-0 [&amp;_svg:not([class*='size-'])]:size-4"
                  tabindex="-1"
                  data-orientation="horizontal"
                  data-radix-collection-item=""
                >
                  게시글 (0)</button
                ><button
                  type="button"
                  role="tab"
                  aria-selected="false"
                  aria-controls="radix-:r5:-content-qna"
                  data-state="inactive"
                  id="radix-:r5:-trigger-qna"
                  data-slot="tabs-trigger"
                  class="data-[state=active]:bg-card dark:data-[state=active]:text-foreground focus-visible:border-ring focus-visible:ring-ring/50 focus-visible:outline-ring dark:data-[state=active]:border-input dark:data-[state=active]:bg-input/30 text-foreground dark:text-muted-foreground inline-flex h-[calc(100%-1px)] flex-1 items-center justify-center gap-1.5 rounded-xl border border-transparent px-2 py-1 text-sm font-medium whitespace-nowrap transition-[color,box-shadow] focus-visible:ring-[3px] focus-visible:outline-1 disabled:pointer-events-none disabled:opacity-50 [&amp;_svg]:pointer-events-none [&amp;_svg]:shrink-0 [&amp;_svg:not([class*='size-'])]:size-4"
                  tabindex="-1"
                  data-orientation="horizontal"
                  data-radix-collection-item=""
                >
                  Q&amp;A (1)
                </button>
              </div>
              <div
                data-state="active"
                data-orientation="horizontal"
                role="tabpanel"
                aria-labelledby="radix-:r5:-trigger-all"
                id="radix-:r5:-content-all"
                tabindex="0"
                data-slot="tabs-content"
                class="flex-1 outline-none mt-8"
                style=""
              >
                <div
                  class="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-6"
                >
                  <div
                    data-slot="card"
                    class="bg-card text-card-foreground flex flex-col gap-6 rounded-xl border group hover:shadow-xl transition-all duration-300 overflow-hidden cursor-pointer hover:ring-2 hover:ring-orange-100"
                  >
                    <div class="relative">
                      <img
                        src="https://images.unsplash.com/photo-1708388064278-707e85eaddc0?crop=entropy&amp;cs=tinysrgb&amp;fit=max&amp;fm=jpg&amp;ixid=M3w3Nzg4Nzd8MHwxfHNlYXJjaHwxfHxrb3JlYW4lMjBmb29kJTIwa2ltY2hpJTIwamppZ2FlfGVufDF8fHx8MTc1NzU3OTAxOHww&amp;ixlib=rb-4.1.0&amp;q=80&amp;w=1080"
                        alt="집에서 만드는 김치찌개"
                        class="w-full h-48 object-cover group-hover:scale-105 transition-transform duration-300"
                      />
                      <div class="absolute top-3 left-3">
                        <span
                          data-slot="badge"
                          class="inline-flex items-center justify-center rounded-md border px-2 py-0.5 text-xs font-medium w-fit whitespace-nowrap shrink-0 [&amp;&gt;svg]:size-3 gap-1 [&amp;&gt;svg]:pointer-events-none focus-visible:border-ring focus-visible:ring-ring/50 focus-visible:ring-[3px] aria-invalid:ring-destructive/20 dark:aria-invalid:ring-destructive/40 aria-invalid:border-destructive transition-[color,box-shadow] overflow-hidden border-transparent [a&amp;]:hover:bg-primary/90 bg-green-500 text-white"
                          >레시피</span
                        >
                      </div>
                      <div class="absolute top-3 right-3">
                        <span
                          data-slot="badge"
                          class="inline-flex items-center justify-center rounded-md border px-2 py-0.5 text-xs font-medium w-fit whitespace-nowrap shrink-0 [&amp;&gt;svg]:size-3 gap-1 [&amp;&gt;svg]:pointer-events-none focus-visible:border-ring focus-visible:ring-ring/50 focus-visible:ring-[3px] aria-invalid:ring-destructive/20 dark:aria-invalid:ring-destructive/40 aria-invalid:border-destructive transition-[color,box-shadow] overflow-hidden border-transparent [a&amp;]:hover:bg-primary/90 bg-white/90 text-gray-700"
                          >한식</span
                        >
                      </div>
                    </div>
                    <div
                      data-slot="card-content"
                      class="[&amp;:last-child]:pb-6 p-4"
                    >
                      <h3
                        class="font-semibold mb-2 group-hover:text-orange-500 transition-colors line-clamp-1"
                      >
                        집에서 만드는 김치찌개
                      </h3>
                      <p
                        class="text-sm text-muted-foreground mb-3 line-clamp-2"
                      >
                        매콤하고 진한 김치찌개 만들기, 집에서도 맛집처럼!
                      </p>
                      <div class="flex items-center space-x-2 mb-3 text-sm">
                        <svg
                          xmlns="http://www.w3.org/2000/svg"
                          width="24"
                          height="24"
                          viewBox="0 0 24 24"
                          fill="none"
                          stroke="currentColor"
                          stroke-width="2"
                          stroke-linecap="round"
                          stroke-linejoin="round"
                          class="lucide lucide-user h-4 w-4 text-muted-foreground"
                          aria-hidden="true"
                        >
                          <path
                            d="M19 21v-2a4 4 0 0 0-4-4H9a4 4 0 0 0-4 4v2"
                          ></path>
                          <circle cx="12" cy="7" r="4"></circle></svg
                        ><span class="text-muted-foreground">by 김요리사</span
                        ><span
                          data-slot="badge"
                          class="inline-flex items-center justify-center rounded-md border px-2 py-0.5 text-xs font-medium w-fit whitespace-nowrap shrink-0 [&amp;&gt;svg]:size-3 gap-1 [&amp;&gt;svg]:pointer-events-none focus-visible:border-ring focus-visible:ring-ring/50 focus-visible:ring-[3px] aria-invalid:ring-destructive/20 dark:aria-invalid:ring-destructive/40 aria-invalid:border-destructive transition-[color,box-shadow] overflow-hidden [a&amp;]:hover:bg-accent [a&amp;]:hover:text-accent-foreground bg-emerald-100 text-emerald-700"
                          >나무</span
                        >
                      </div>
                      <div
                        class="flex items-center space-x-1 text-muted-foreground text-sm mb-3"
                      >
                        <svg
                          xmlns="http://www.w3.org/2000/svg"
                          width="24"
                          height="24"
                          viewBox="0 0 24 24"
                          fill="none"
                          stroke="currentColor"
                          stroke-width="2"
                          stroke-linecap="round"
                          stroke-linejoin="round"
                          class="lucide lucide-clock h-4 w-4"
                          aria-hidden="true"
                        >
                          <path d="M12 6v6l4 2"></path>
                          <circle cx="12" cy="12" r="10"></circle></svg
                        ><span>조리시간: 30분</span><span class="mx-2">•</span
                        ><span>난이도: 보통</span>
                      </div>
                      <div class="flex flex-wrap gap-1 mt-3">
                        <button
                          class="text-xs bg-orange-100 text-orange-700 px-2 py-1 rounded-full hover:bg-orange-200 transition-colors"
                        >
                          #김치찌개</button
                        ><button
                          class="text-xs bg-orange-100 text-orange-700 px-2 py-1 rounded-full hover:bg-orange-200 transition-colors"
                        >
                          #한식</button
                        ><button
                          class="text-xs bg-orange-100 text-orange-700 px-2 py-1 rounded-full hover:bg-orange-200 transition-colors"
                        >
                          #집밥
                        </button>
                      </div>
                    </div>
                  </div>
                  <div
                    data-slot="card"
                    class="bg-card text-card-foreground flex flex-col gap-6 rounded-xl border group hover:shadow-xl transition-all duration-300 overflow-hidden cursor-pointer hover:ring-2 hover:ring-orange-100"
                  >
                    <div
                      data-slot="card-content"
                      class="[&amp;:last-child]:pb-6 p-4"
                    >
                      <div class="flex items-center justify-between mb-3">
                        <span
                          data-slot="badge"
                          class="inline-flex items-center justify-center rounded-md border px-2 py-0.5 text-xs font-medium w-fit whitespace-nowrap shrink-0 [&amp;&gt;svg]:size-3 gap-1 [&amp;&gt;svg]:pointer-events-none focus-visible:border-ring focus-visible:ring-ring/50 focus-visible:ring-[3px] aria-invalid:ring-destructive/20 dark:aria-invalid:ring-destructive/40 aria-invalid:border-destructive transition-[color,box-shadow] overflow-hidden border-transparent [a&amp;]:hover:bg-primary/90 bg-purple-500 text-white"
                          >Q&amp;A</span
                        ><span
                          data-slot="badge"
                          class="inline-flex items-center justify-center rounded-md border px-2 py-0.5 text-xs font-medium w-fit whitespace-nowrap shrink-0 [&amp;&gt;svg]:size-3 gap-1 [&amp;&gt;svg]:pointer-events-none focus-visible:border-ring focus-visible:ring-ring/50 focus-visible:ring-[3px] aria-invalid:ring-destructive/20 dark:aria-invalid:ring-destructive/40 aria-invalid:border-destructive transition-[color,box-shadow] overflow-hidden border-transparent [a&amp;]:hover:bg-primary/90 bg-white/90 text-gray-700"
                          >한식</span
                        >
                      </div>
                      <h3
                        class="font-semibold mb-2 group-hover:text-orange-500 transition-colors line-clamp-1"
                      >
                        김치찌개에 설탕을 넣어도 되나요?
                      </h3>
                      <p
                        class="text-sm text-muted-foreground mb-3 line-clamp-2"
                      >
                        김치찌개를 끓일 때 신맛을 줄이기 위해 설탕을 넣어도
                        괜찮을까요? 다른 방법이 있다면 알려주세요.
                      </p>
                      <div class="flex items-center space-x-2 mb-3 text-sm">
                        <svg
                          xmlns="http://www.w3.org/2000/svg"
                          width="24"
                          height="24"
                          viewBox="0 0 24 24"
                          fill="none"
                          stroke="currentColor"
                          stroke-width="2"
                          stroke-linecap="round"
                          stroke-linejoin="round"
                          class="lucide lucide-user h-4 w-4 text-muted-foreground"
                          aria-hidden="true"
                        >
                          <path
                            d="M19 21v-2a4 4 0 0 0-4-4H9a4 4 0 0 0-4 4v2"
                          ></path>
                          <circle cx="12" cy="7" r="4"></circle></svg
                        ><span class="text-muted-foreground">by 요리초보</span
                        ><span
                          data-slot="badge"
                          class="inline-flex items-center justify-center rounded-md border px-2 py-0.5 text-xs font-medium w-fit whitespace-nowrap shrink-0 [&amp;&gt;svg]:size-3 gap-1 [&amp;&gt;svg]:pointer-events-none focus-visible:border-ring focus-visible:ring-ring/50 focus-visible:ring-[3px] aria-invalid:ring-destructive/20 dark:aria-invalid:ring-destructive/40 aria-invalid:border-destructive transition-[color,box-shadow] overflow-hidden [a&amp;]:hover:bg-accent [a&amp;]:hover:text-accent-foreground bg-yellow-100 text-yellow-700"
                          >씨앗</span
                        >
                      </div>
                      <div class="flex flex-wrap gap-1 mt-3">
                        <button
                          class="text-xs bg-orange-100 text-orange-700 px-2 py-1 rounded-full hover:bg-orange-200 transition-colors"
                        >
                          #김치찌개</button
                        ><button
                          class="text-xs bg-orange-100 text-orange-700 px-2 py-1 rounded-full hover:bg-orange-200 transition-colors"
                        >
                          #조리팁</button
                        ><button
                          class="text-xs bg-orange-100 text-orange-700 px-2 py-1 rounded-full hover:bg-orange-200 transition-colors"
                        >
                          #설탕
                        </button>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="text-center mt-12">
              <button
                data-slot="button"
                class="inline-flex items-center justify-center gap-2 whitespace-nowrap rounded-md text-sm font-medium transition-all disabled:pointer-events-none disabled:opacity-50 [&amp;_svg]:pointer-events-none [&amp;_svg:not([class*='size-'])]:size-4 shrink-0 [&amp;_svg]:shrink-0 outline-none focus-visible:border-ring focus-visible:ring-ring/50 focus-visible:ring-[3px] aria-invalid:ring-destructive/20 dark:aria-invalid:ring-destructive/40 aria-invalid:border-destructive h-9 has-[&gt;svg]:px-3 bg-orange-500 hover:bg-gradient-to-r hover:from-pink-500 hover:to-orange-500 text-white px-8 py-3"
              >
                더 많은 검색 결과 보기
              </button>
            </div>
          </div>
        </section>
      </div>
    </main>
  </body>
</html>
