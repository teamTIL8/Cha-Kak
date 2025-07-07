window.onload = () => {
				   const container = document.getElementById('map'); // 지도를 표시할 div
				   const options = {
				     center: new kakao.maps.LatLng(37.5665, 126.9780), //지도의 중심 좌표
				     level: 10 //지도의 확대 레벨
				   };
				   const map = new kakao.maps.Map(container, options);

				//마커 클러스터러 생성
				   const clusterer = new kakao.maps.MarkerClusterer({
				     map: map, // 마커들을 클러스터로 관리하고 표시할 지도 객체 
				     averageCenter: true, // 클러스터에 포함된 마커들의 평균 위치를 클러스터 마커 위치로 설정
				     minLevel: 6 // 클러스터 할 최소 지도 레벨 
				   });
				   
				   fetch('/admin/statistics/map',{
					method: "GET",
					credentials: "include"
				   })
				     .then(res => res.json())
				     .then(data => {
				       const markers = [];

					   let currentInfoWindow = null; // 현재 열려 있는 인포윈도우 추적
					   
				       data.forEach(p => {
				         const marker = new kakao.maps.Marker({
				           position: new kakao.maps.LatLng(p.latitude, p.longitude)
				         });

				         const address = p.address || '주소 없음';

				         const infowindow = new kakao.maps.InfoWindow({
				           content: `<div class="info-title">${address}</div>`, // 인포 윈도우 내에 들어갈 컨텐츠
				           removable: false 
				         });
						 
						 // 클릭시 말풍선 열림 (닫지는 X)
						 kakao.maps.event.addListener(marker, 'click', function () {
						   infowindow.open(map, marker);
						   currentInfoWindow = infowindow;
						   
						   //꼬리/배경 제거
						   setTimeout(() => {
					         const infoTitles = document.querySelectorAll('.info-title');
					         infoTitles.forEach(e => {
					           const parent = e.parentElement;
					           if (parent?.previousSibling) {
					             parent.previousSibling.style.display = 'none'; // 꼬리 제거
					           }
					           if (parent?.parentElement) {
					             parent.parentElement.style.border = 'none';
					             parent.parentElement.style.background = 'unset';
					             parent.parentElement.style.boxShadow = 'none';
					           }
					         });
					       }, 10); // 짧게 지연해서 DOM 붙은 후 실행
						 });
						 
				         markers.push(marker);
				       });

					   // 마커 클러스터에 등록
				       clusterer.addMarkers(markers);
					   
				     })
				     .catch(err => console.error("클러스터링 로딩 실패:", err));
					};

					document.addEventListener("DOMContentLoaded", function () {
					       const select = document.getElementById("violationType");

					       select.addEventListener("change", function () {
					         const type = select.value;
					         if (!type) return;

					         fetch(`/admin/statistics/by-type?violationType=${type}`)
					           .then((res) => {
					             if (!res.ok) throw new Error("서버 응답 오류");
					             return res.json();
					           })
					           .then((data) => {
					             const tableBody = document.querySelector("#reportTable tbody");
					             tableBody.innerHTML = "";

					             if (data.length === 0) {
					               const row = document.createElement("tr");
					               const cell = document.createElement("td");
					               cell.colSpan = 7;
					               cell.innerText = "해당 유형의 제보가 없습니다.";
					               row.appendChild(cell);
					               tableBody.appendChild(row);
					               return;
					             }

					             data.forEach((report, index) => {
					               const row = document.createElement("tr");

					               const region = report.address ?? "-";
					               const date = new Date(report.reportTime).toLocaleString();

					               row.innerHTML = `
												<td>${report.reportId}</td>
												<td>${report.title}</td>
												<td>${report.violationType}</td>
												<td>${region}</td>
												<td>${date}</td>
											`;

					               tableBody.appendChild(row);
					             });
					           })
					           .catch((err) => {
					             console.error("요청 실패:", err);
					             alert("데이터를 불러오는 데 실패했습니다.");
					           });
					       });
					     });

		